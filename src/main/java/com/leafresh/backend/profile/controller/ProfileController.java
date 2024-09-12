package com.leafresh.backend.profile.controller;

import com.leafresh.backend.oauth.exception.ResourceNotFoundException;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.security.CurrentUser;
import com.leafresh.backend.oauth.security.UserPrincipal;
import com.leafresh.backend.profile.model.dto.ProfileDTO;
import com.leafresh.backend.profile.model.entity.ProfileEntity;
import com.leafresh.backend.profile.repository.ProfileRepository;
import com.leafresh.backend.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class ProfileController {


    private ProfileService profileService;
    private UserRepository userRepository;
    private ProfileRepository profileRepository;

    @Autowired
    public ProfileController(ProfileService profileService, UserRepository userRepository, ProfileRepository profileRepository) {
        this.profileService = profileService;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }




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

    @GetMapping("/info-by-nickname")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfileDTO> getProfileByUserNickname(@RequestParam("nickname") String userNickname) {
        // 1. 닉네임으로 유저 정보 조회
        System.out.println("닉네임으로 유저 정보 조회 시도: " + userNickname);
        Optional<User> user = userRepository.findByUserNickname(userNickname);

        if (user.isEmpty()) {  // 변경된 부분: isEmpty()로 체크
            System.out.println("유저 정보가 존재하지 않음: " + userNickname);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 2. 유저 ID로 프로필 정보 조회
        Integer userId = user.get().getUserId();
        System.out.println("유저 ID로 프로필 정보 조회 시도: userId=" + userId);
        Optional<ProfileEntity> profile = profileRepository.findByUserUserId(userId);

        if (profile.isPresent()) {
            ProfileEntity profileEntity = profile.get();
            System.out.println("프로필 정보 조회 성공: userId=" + userId +
                    ", profileTitle=" + profileEntity.getProfileTitle() +
                    ", profileDescription=" + profileEntity.getProfileDescription());
            ProfileDTO profileDTO = new ProfileDTO(profileEntity.getProfileTitle(), profileEntity.getProfileDescription());
            return ResponseEntity.ok(profileDTO);  // ProfileEntity에서 ProfileDTO로 변환 후 반환
        } else {
            System.out.println("프로필 정보가 존재하지 않음: userId=" + userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
