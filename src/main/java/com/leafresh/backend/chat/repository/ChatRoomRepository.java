package com.leafresh.backend.chat.repository;



import com.leafresh.backend.chat.model.entity.ChatRoom;
import com.leafresh.backend.oauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    ChatRoom findByUser1AndUser2(User user1, User user2);
}
