package com.leafresh.backend.follow.repository;

import com.leafresh.backend.follow.model.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // 수정된 부분
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Integer> {

    boolean existsByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId);

    // 단일 Optional 반환에서 List로 변경
    List<FollowEntity> findAllByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId); // 수정된 부분
}
