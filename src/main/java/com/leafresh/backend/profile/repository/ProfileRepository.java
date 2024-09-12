package com.leafresh.backend.profile.repository;

import com.leafresh.backend.profile.model.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByUserUserId(Integer userId);
    Optional<ProfileEntity> findByUserUserNickname(String userNickname);
}
