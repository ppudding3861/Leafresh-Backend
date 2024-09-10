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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
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
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        // userPrincipal에서 userId를 가져올 때 적절한 메서드 사용
        Integer userId = userPrincipal.getUserId();

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    // 비밀번호 검증 API
    @PostMapping("/verify-password")
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
    public ResponseEntity<?> updateUser(@CurrentUser UserPrincipal userPrincipal, @RequestBody Map<String, String> request) {
        try {
            User updatedUser = customUserDetailsService.updateUser(userPrincipal.getUserId(), request);
            return ResponseEntity.ok(new ApiResponse(true, "사용자 정보가 성공적으로 업데이트되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
        }
    }
}
