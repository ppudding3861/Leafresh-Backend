package com.leafresh.backend.chat_with_spring.chat.controller;

import com.leafresh.backend.chat_with_spring.chat.model.ChatMessage;
import com.leafresh.backend.chat_with_spring.chat.service.ChatMessageService;
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
    private final ChatMessageService chatMessageService;

    public ChatController(SimpMessageSendingOperations messageSendingOperations, ChatMessageService chatMessageService) {
        this.messageSendingOperations = messageSendingOperations;
        this.chatMessageService = chatMessageService;
    }

    // 특정 채팅방의 메시지 조회
    @GetMapping("/chatroom/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable Long chatRoomId) {
        List<ChatMessage> messages = chatMessageService.getMessagesByChatRoomId(chatRoomId);
        return ResponseEntity.ok(messages);
    }

    // 새 메시지 수신 및 저장
    @MessageMapping("/message")
    public void receiveMessage(@Payload ChatMessage chatMessage) {
        if (chatMessage.getChatRoomId() == null) {
            System.err.println("채팅방 ID가 누락되었습니다.");
            return;
        }

        System.out.println("수신한 메시지: " + chatMessage);

        // 메시지를 해당 채팅방으로 전송
        String chatRoomId = chatMessage.getChatRoomId().toString();
        messageSendingOperations.convertAndSend("/sub/chatroom/" + chatRoomId, chatMessage);

        // 메시지 저장
        try {
            ChatMessage savedMessage = chatMessageService.saveMessage(chatMessage);
            System.out.println("저장된 메시지: " + savedMessage);
        } catch (Exception e) {
            System.err.println("메시지 저장 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
