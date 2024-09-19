package com.leafresh.backend.follow.repository;

import com.leafresh.backend.follow.model.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Integer> {

    // 팔로잉 여부 확인
    boolean existsByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId);

    // 팔로우 관계를 가져오는 메서드
    List<FollowEntity> findAllByFollowerUserIdAndFollowingUserId(Integer followerId, Integer followingId);

    // 팔로워 리스트를 가져오는 메서드 추가
    List<FollowEntity> findAllByFollowingUserId(Integer followingUserId);

    // 팔로잉 리스트를 가져오는 메서드 추가
    List<FollowEntity> findAllByFollowerUserId(Integer followerUserId);

    // 팔로워 수를 계산하는 메서드
    int countByFollowingUserId(Integer followingUserId);
}
