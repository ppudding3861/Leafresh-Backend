package com.leafresh.backend.chat_with_spring.chat.service;

import com.leafresh.backend.chat_with_spring.chat.model.ChatMessage;
import com.leafresh.backend.chat_with_spring.chat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // 특정 채팅방의 메시지 조회
    public List<ChatMessage> getMessagesByChatRoomId(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }

    // 메시지 저장
    public ChatMessage saveMessage(ChatMessage chatMessage) {
        System.out.println("메시지 저장 시도: " + chatMessage);
        return chatMessageRepository.save(chatMessage);
    }

}
