package com.leafresh.backend.follow.service;

import com.leafresh.backend.follow.model.entity.FollowEntity;
import com.leafresh.backend.follow.repository.FollowRepository;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    // 팔로우 상태 확인
    @Transactional(readOnly = true)
    public boolean isFollowing(Integer userId, String followingNickname) {
        User following = userRepository.findByUserNickname(followingNickname)
                .orElseThrow(() -> new ResourceNotFoundException("User", "nickname", followingNickname));
        return followRepository.existsByFollowerUserIdAndFollowingUserId(userId, following.getUserId());
    }

    // 팔로우 추가
    @Transactional
    public void followUser(Integer followerId, String followingNickname) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", followerId));
        User following = userRepository.findByUserNickname(followingNickname)
                .orElseThrow(() -> new ResourceNotFoundException("User", "nickname", followingNickname));

        FollowEntity followEntity = new FollowEntity.Builder()
                .follower(follower)
                .following(following)
                .build();
        followRepository.save(followEntity);
    }

    // 팔로우 취소
    @Transactional
    public void unfollowUser(Integer followerId, String followingNickname) {
        User following = userRepository.findByUserNickname(followingNickname)
                .orElseThrow(() -> new ResourceNotFoundException("User", "nickname", followingNickname));

        FollowEntity followEntity = followRepository.findByFollowerUserIdAndFollowingUserId(followerId, following.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Follow", "followerId and followingId", followerId + " and " + following.getUserId()));

        followRepository.delete(followEntity);
    }
}
