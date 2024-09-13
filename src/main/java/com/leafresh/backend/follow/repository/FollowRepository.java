package com.leafresh.backend.follow.repository;

import com.leafresh.backend.follow.model.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Integer> {

    // follower와 following의 User의 id 필드에 접근하도록 수정
    boolean existsByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId);

    Optional<FollowEntity> findByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId);
}
