package com.leafresh.backend.profile.controller;

import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.profile.model.dto.ProfileDTO;
import com.leafresh.backend.profile.model.entity.ProfileEntity;
import com.leafresh.backend.profile.repository.ProfileRepository;
import com.leafresh.backend.profile.service.ProfileService;
import com.leafresh.backend.oauth.security.CurrentUser;
import com.leafresh.backend.oauth.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("/check")
    public ResponseEntity<?> checkProfile(@CurrentUser UserPrincipal userPrincipal) {
        boolean exists = profileService.checkProfileExists(userPrincipal.getUserId());
        return ResponseEntity.ok().body(Map.of("exists", exists));
    }
}
