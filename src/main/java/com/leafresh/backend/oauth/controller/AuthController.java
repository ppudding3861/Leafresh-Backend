package com.leafresh.backend.oauth.controller;

import com.leafresh.backend.oauth.exception.BadRequestException;
import com.leafresh.backend.oauth.model.Role;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.model.UserStatus;
import com.leafresh.backend.oauth.payload.ApiResponse;
import com.leafresh.backend.oauth.payload.AuthResponse;
import com.leafresh.backend.oauth.payload.LoginRequest;
import com.leafresh.backend.oauth.payload.SignUpRequest;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.security.TokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    /**
     * 사용자 로그인 처리
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUserMailAdress(signUpRequest.getEmail())) {
            throw new BadRequestException("이미 사용 중인 이메일입니다."); // 오류 메시지를 더 명확하게 수정
        }

        try {
            // 사용자 계정 생성 로직
            User user = new User();
            user.setUserName(signUpRequest.getName());
            user.setUserNickname(signUpRequest.getNickname());
            user.setUserPhoneNumber(signUpRequest.getPhoneNumber());
            user.setUserMailAdress(signUpRequest.getEmail());
            user.setUserPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            user.setUserJoinDate(new Date());
            user.setUserReportCount(0);
            user.setUserStatus(UserStatus.ACTIVE);
            user.setRole(Role.USER);

            if (signUpRequest.getImageUrl() != null) {
                user.setImageUrl(signUpRequest.getImageUrl());
            }

            User result = userRepository.save(user);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/user/me")
                    .buildAndExpand(result.getUserId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "회원가입이 성공적으로 완료되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "회원가입 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    /**
     * 사용자 로그아웃 처리
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // 클라이언트에서 로그아웃 처리를 위한 간단한 응답 반환
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiResponse(true, "사용자가 성공적으로 로그아웃되었습니다."));
    }
}
