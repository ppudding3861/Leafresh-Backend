package com.leafresh.backend.chat.repository;


import com.leafresh.backend.chat.model.entity.ChatMessage;
import com.leafresh.backend.chat.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findByChatRoomOrderByTimestampAsc(ChatRoom chatRoom);
    void deleteByChatRoom(ChatRoom chatRoom);
}
