package com.leafresh.backend.follow.service;

import com.leafresh.backend.follow.model.dto.FollowDTO;
import com.leafresh.backend.follow.model.entity.FollowEntity;
import com.leafresh.backend.follow.repository.FollowRepository;
import com.leafresh.backend.ftp.service.FtpImgLoaderUtil2;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.exception.ResourceNotFoundException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FtpImgLoaderUtil2 ftpImgLoaderUtil2;

    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository, FtpImgLoaderUtil2 ftpImgLoaderUtil2) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.ftpImgLoaderUtil2 = ftpImgLoaderUtil2;
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

    // 팔로워 수를 가져오는 메서드
    @Transactional(readOnly = true)
    public int getFollowersCount(String nickname) {
        User user = userRepository.findByUserNickname(nickname)
                .orElseThrow(() -> new ResourceNotFoundException("User", "nickname", nickname));

        return followRepository.countByFollowingUserId(user.getUserId());
    }

    // FTP 서버에서 이미지를 가져오는 메서드
    private String getFtpImageUrl(String imagePath) {
        try {
            Resource imageResource = ftpImgLoaderUtil2.download(imagePath);
            if (imageResource != null && imageResource.exists()) {
                byte[] imageBytes = IOUtils.toByteArray(imageResource.getInputStream());
                return Base64.getEncoder().encodeToString(imageBytes);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // 팔로워 리스트 조회
    @Transactional(readOnly = true)
    public List<FollowDTO> getFollowers(Integer userId) {
        List<FollowEntity> followers = followRepository.findAllByFollowingUserId(userId);
        return followers.stream().map(follow -> {
            String imageUrl = getFtpImageUrl(follow.getFollower().getImageUrl()); // FTP 서버에서 이미지 가져오기
            return new FollowDTO(follow.getFollower().getUserNickname(), imageUrl, false);
        }).collect(Collectors.toList());
    }

    // 팔로잉 리스트 조회
    @Transactional(readOnly = true)
    public List<FollowDTO> getFollowing(Integer userId) {
        List<FollowEntity> following = followRepository.findAllByFollowerUserId(userId);
        return following.stream().map(follow -> {
            String imageUrl = getFtpImageUrl(follow.getFollowing().getImageUrl()); // FTP 서버에서 이미지 가져오기
            return new FollowDTO(follow.getFollowing().getUserNickname(), imageUrl, true);
        }).collect(Collectors.toList());
    }
}
