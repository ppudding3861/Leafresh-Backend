package com.leafresh.backend.chat.repository;

import com.leafresh.backend.oauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<User, Integer> {
    // 수정: findByUserName으로 변경
    Optional<User> findByUserName(String userName);
}
