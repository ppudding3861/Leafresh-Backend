package com.leafresh.backend.follow.service;

import com.leafresh.backend.follow.model.entity.FollowEntity;
import com.leafresh.backend.follow.repository.FollowRepository;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public boolean isFollowing(Integer userId, String followingNickname) {
        User following = userRepository.findByUserNickname(followingNickname)
                .orElseThrow(() -> new ResourceNotFoundException("User", "nickname", followingNickname));
        return followRepository.existsByFollowerUserIdAndFollowingUserId(userId, following.getUserId());
    }

    @Transactional
    public void followUser(Integer followerId, String followingNickname) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", followerId));
        User following = userRepository.findByUserNickname(followingNickname)
                .orElseThrow(() -> new ResourceNotFoundException("User", "nickname", followingNickname));

        // 이미 팔로잉 중인지 확인하여 중복 팔로잉을 방지
        if (!isFollowing(followerId, followingNickname)) {
            FollowEntity followEntity = new FollowEntity.Builder()
                    .follower(follower)
                    .following(following)
                    .build();
            followRepository.save(followEntity);
        }
    }

    @Transactional
    public void unfollowUser(Integer followerId, String followingNickname) {
        User following = userRepository.findByUserNickname(followingNickname)
                .orElseThrow(() -> new ResourceNotFoundException("User", "nickname", followingNickname));

        List<FollowEntity> followEntities = followRepository.findAllByFollowerUserIdAndFollowingUserId(followerId, following.getUserId());
        if (followEntities.isEmpty()) {
            throw new ResourceNotFoundException("Follow", "followerId and followingId", followerId + " and " + following.getUserId());
        }

        followRepository.deleteAll(followEntities);
    }

    // 팔로워 수를 가져오는 메서드 추가
    @Transactional(readOnly = true)
    public int getFollowersCount(String nickname) {
        User user = userRepository.findByUserNickname(nickname)
                .orElseThrow(() -> new ResourceNotFoundException("User", "nickname", nickname));

        return followRepository.countByFollowingUserId(user.getUserId()); // 팔로워 수를 반환
    }


}
