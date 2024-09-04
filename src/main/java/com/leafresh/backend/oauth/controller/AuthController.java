package com.leafresh.backend.oauth.controller;

import com.leafresh.backend.oauth.exception.BadRequestException;
import com.leafresh.backend.oauth.payload.ApiResponse;
import com.leafresh.backend.oauth.payload.AuthResponse;
import com.leafresh.backend.oauth.payload.LoginRequest;
import com.leafresh.backend.oauth.payload.SignUpRequest;
import com.leafresh.backend.oauth.security.CustomUserDetailsService;
import com.leafresh.backend.oauth.security.TokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // CustomUserDetailsService 주입

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
        // 회원가입 로직을 CustomUserDetailsService의 registerUser 메서드로 위임
        return customUserDetailsService.registerUser(signUpRequest);
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
