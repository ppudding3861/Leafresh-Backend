package com.leafresh.backend.oauth.repository;

import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.profile.model.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {  // Long을 Integer로 수정

    Optional<User> findByUserMailAdress(String userMailAdress);  // 메서드명과 필드명 수정
    Boolean existsByUserMailAdress(String userMailAdress);  // 메서드명과 필드명 수정
    Optional<User> findByUserNickname(String userNickname);
    Boolean existsByUserNickname(String userNickname);
}
