package com.leafresh.backend.profile.controller;

import com.leafresh.backend.oauth.security.CurrentUser;
import com.leafresh.backend.oauth.security.UserPrincipal;
import com.leafresh.backend.profile.model.dto.ProfileDTO;
import com.leafresh.backend.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/add")
    public ResponseEntity<?> createProfile(@CurrentUser UserPrincipal userPrincipal, @RequestBody ProfileDTO profileDTO) {
        profileService.createProfile(userPrincipal.getUserId(), profileDTO);
        return ResponseEntity.ok().body("프로필이 성공적으로 등록되었습니다.");
    }

    @GetMapping("/info")
    public ResponseEntity<?> getProfileInfo(@CurrentUser UserPrincipal userPrincipal) {
        try {
            ProfileDTO profileDTO = profileService.getProfileInfo(userPrincipal.getUserId());
            return ResponseEntity.ok(profileDTO); // 프로필 정보 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("정보가 없습니다."); // 프로필이 없는 경우 에러 메시지 반환
        }
    }

    @PostMapping("/modify")
    public ResponseEntity<?> modifyProfile(@CurrentUser UserPrincipal userPrincipal, @RequestBody ProfileDTO profileDTO) {
        profileService.modifyProfile(userPrincipal.getUserId(), profileDTO);
        return ResponseEntity.ok().body("프로필이 성공적으로 수정되었습니다.");
    }
}
