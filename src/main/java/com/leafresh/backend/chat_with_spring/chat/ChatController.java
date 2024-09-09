package com.leafresh.backend.chat_with_spring.chat;

import com.leafresh.backend.chat_with_spring.chat.model.ChatMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {
    private final SimpMessageSendingOperations messageSendingOperations;
    private final ChatMessageRepository chatMessageRepository;

    public ChatController(SimpMessageSendingOperations messageSendingOperations, ChatMessageRepository chatMessageRepository) {
        this.messageSendingOperations = messageSendingOperations;
        this.chatMessageRepository = chatMessageRepository;
    }

    @GetMapping("/chat/{id}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable Long id) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(id);
        return ResponseEntity.ok(messages);
    }

    @MessageMapping("/message")
    public void receiveMessage(@Payload ChatMessage chatMessage) {
        if (chatMessage.getChatRoomId() == null) {
            System.err.println("채팅방 ID가 누락되었습니다.");
            return;
        }

        System.out.println("수신한 메시지: " + chatMessage); // 로그 추가
        String chatRoomId = chatMessage.getChatRoomId().toString();
        messageSendingOperations.convertAndSend("/sub/chatroom/" + chatRoomId, chatMessage);

        // 메시지 저장
        try {
            ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
            System.out.println("저장된 메시지: " + savedMessage); // 로그 추가
        } catch (Exception e) {
            System.err.println("메시지 저장 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
