package com.leafresh.backend.follow.repository;

import com.leafresh.backend.follow.model.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Integer> {

    boolean existsByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId);

    List<FollowEntity> findAllByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId);

    // 팔로워 수를 계산하는 메서드 추가
    int countByFollowingUserId(Integer followingUserId); // 팔로워 수 계산

    List<FollowEntity> findAllByFollowingUserId(Integer followingId);
}
