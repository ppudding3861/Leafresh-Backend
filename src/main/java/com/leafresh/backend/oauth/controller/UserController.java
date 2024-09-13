package com.leafresh.backend.oauth.controller;

import com.leafresh.backend.oauth.exception.ResourceNotFoundException;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.payload.ApiResponse;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.security.CurrentUser;
import com.leafresh.backend.oauth.security.UserPrincipal;
import com.leafresh.backend.oauth.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;
    private CustomUserDetailsService customUserDetailsService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserRepository userRepository, CustomUserDetailsService customUserDetailsService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        Integer userId = userPrincipal.getUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @GetMapping("/info-market")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getUserProfileByEmail(@RequestParam String email) {
        User user = userRepository.findByUserMailAdress(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/verify-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verifyPassword(@CurrentUser UserPrincipal userPrincipal, @RequestBody Map<String, String> request) {
        User user = customUserDetailsService.loadUserEntityById(userPrincipal.getUserId());
        String currentPassword = request.get("password");
        if (customUserDetailsService.verifyPassword(user, currentPassword)) {
            return ResponseEntity.ok(new ApiResponse(true, "비밀번호 일치"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "비밀번호 불일치"));
        }
    }

    @PostMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUser(@CurrentUser UserPrincipal userPrincipal, @RequestBody Map<String, String> request) {
        try {
            User updatedUser = customUserDetailsService.updateUser(userPrincipal.getUserId(), request);
            return ResponseEntity.ok(new ApiResponse(true, "사용자 정보가 성공적으로 업데이트되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/info-by-nickname")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserProfileByNickname(@RequestParam String nickname) {
        User user = userRepository.findByUserNickname(nickname)
                .orElseThrow(() -> new ResourceNotFoundException("User", "nickname", nickname));
        return ResponseEntity.ok(Map.of(
                "userName", user.getUserName(),
                "imageUrl", user.getImageUrl(),
                "followers", user.getFollowers().size(),
                "userEmail", user.getUserMailAdress()
        ));
    }
}
