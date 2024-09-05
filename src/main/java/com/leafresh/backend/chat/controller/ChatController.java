package com.leafresh.backend.chat.controller;

import com.leafresh.backend.chat.model.dto.ChatMessageDTO;
import com.leafresh.backend.chat.model.entity.ChatMessage;
import com.leafresh.backend.chat.model.entity.ChatRoom;
import com.leafresh.backend.chat.repository.ChatUserRepository;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.chat.service.ChatService;
import com.leafresh.backend.oauth.payload.ApiResponse;
import com.leafresh.backend.oauth.security.CurrentUser;
import com.leafresh.backend.oauth.security.UserPrincipal;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController // @RestController를 사용하여 REST API 응답을 처리
@RequestMapping("/chat")
@CrossOrigin(origins = "${app.cors.allowedOrigins}")  // application.yml에서 가져온 값 사용
public class ChatController {

    @Autowired
    private ChatUserRepository userRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> index(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please login.");
        }
        return ResponseEntity.ok("index");
    }

    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> chatRoom(@PathVariable Integer roomId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please login.");
        }

        ChatRoom chatRoom = chatService.getChatRoomById(roomId);
        User otherUser = chatRoom.getUser1().getUserId().equals(currentUser.getUserId())
                ? chatRoom.getUser2()
                : chatRoom.getUser1();

        Map<String, Object> response = new HashMap<>();
        response.put("chatRoomId", roomId);
        response.put("messages", chatService.getChatMessages(roomId).stream().map(this::convertToDTO).collect(Collectors.toList()));
        response.put("otherUser", otherUser);
        response.put("currentUser", currentUser);
        return ResponseEntity.ok(response);
    }

    @MessageMapping("/chat/{roomId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> sendMessage(@DestinationVariable Integer roomId, ChatMessageDTO messageDTO) {
        if (messageDTO.getSenderId() == null || messageDTO.getRecipientId() == null) {
            return ResponseEntity.badRequest().build();
        }

        ChatRoom chatRoom = chatService.findChatRoomById(roomId);
        if (chatRoom == null) {
            return ResponseEntity.notFound().build();
        }

        User sender = chatService.findUserById(messageDTO.getSenderId());
        User recipient = chatService.findUserById(messageDTO.getRecipientId());

        ChatMessage chatMessage = new ChatMessage(chatRoom, sender, recipient, messageDTO.getContent());
        chatMessage.setTimestamp(LocalDateTime.now());
        chatService.saveMessage(chatMessage);

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, chatMessage); // 메시지 전송 경로
        return ResponseEntity.ok().build();
    }

    @PostMapping("/leave")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> leaveChatRoom(@RequestParam Integer chatRoomId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
        }
        chatService.deleteChatRoom(chatRoomId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<User>> userList(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }
        List<User> users = userRepository.findAll();
        users.removeIf(user -> user.getUserId().equals(currentUser.getUserId()));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/messages/{chatRoomId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(@PathVariable Integer chatRoomId) {
        List<ChatMessage> messages = chatService.getChatMessages(chatRoomId);
        List<ChatMessageDTO> messageDTOs = messages.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }

    private ChatMessageDTO convertToDTO(ChatMessage message) {
        return new ChatMessageDTO(
                message.getId(),
                message.getSender().getUserId(),
                message.getRecipient().getUserId(),
                message.getContent(),
                message.getTimestamp()
        );
    }
    @PostMapping("/chat/create")
    public ResponseEntity<?> createChatRoom(@RequestBody Map<String, Integer> request, @CurrentUser UserPrincipal userPrincipal) {
        Integer postId = request.get("postId");
        Integer userId = userPrincipal.getUserId();

        // 채팅방 생성 로직
        ChatRoom chatRoom = chatService.createChatRoom(postId, userId);

        if (chatRoom != null) {
            return ResponseEntity.ok(Map.of("chatRoomId", chatRoom.getId()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "채팅방 생성 실패"));
        }
    }

}
