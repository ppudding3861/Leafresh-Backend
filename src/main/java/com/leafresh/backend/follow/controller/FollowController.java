package com.leafresh.backend.follow.controller;

import com.leafresh.backend.follow.model.dto.FollowDTO;
import com.leafresh.backend.follow.service.FollowService;
import com.leafresh.backend.oauth.security.CurrentUser;
import com.leafresh.backend.oauth.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    // 팔로우 상태 확인 API
    @GetMapping("/status")
    public ResponseEntity<?> getFollowStatus(@CurrentUser UserPrincipal currentUser, @RequestParam String followingNickname) {
        boolean isFollowing = followService.isFollowing(currentUser.getUserId(), followingNickname);
        return ResponseEntity.ok(new FollowDTO(followingNickname, null, isFollowing));
    }

    // 팔로우 추가 API
    @PostMapping
    public ResponseEntity<?> followUser(@CurrentUser UserPrincipal currentUser, @RequestBody FollowDTO followDTO) {
        followService.followUser(currentUser.getUserId(), followDTO.getUserNickname());
        return ResponseEntity.ok().build();
    }

    // 팔로우 취소 API
    @DeleteMapping
    public ResponseEntity<?> unfollowUser(@CurrentUser UserPrincipal currentUser, @RequestBody FollowDTO followDTO) {
        followService.unfollowUser(currentUser.getUserId(), followDTO.getUserNickname());
        return ResponseEntity.ok().build();
    }

    // 팔로워 수 조회 API
    @GetMapping("/followers/count")
    public ResponseEntity<Integer> getFollowersCount(@RequestParam String nickname) {
        int followersCount = followService.getFollowersCount(nickname);
        return ResponseEntity.ok(followersCount);
    }

    // 팔로워 리스트 조회 API
    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(@CurrentUser UserPrincipal currentUser) {
        List<FollowDTO> followers = followService.getFollowers(currentUser.getUserId());
        return ResponseEntity.ok(followers);
    }

    // 팔로잉 리스트 조회 API
    @GetMapping("/following")
    public ResponseEntity<?> getFollowing(@CurrentUser UserPrincipal currentUser) {
        List<FollowDTO> following = followService.getFollowing(currentUser.getUserId());
        return ResponseEntity.ok(following);
    }


}
