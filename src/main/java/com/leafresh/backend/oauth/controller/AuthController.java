package com.leafresh.backend.oauth.controller;

import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.payload.ApiResponse;
import com.leafresh.backend.oauth.payload.AuthResponse;
import com.leafresh.backend.oauth.payload.LoginRequest;
import com.leafresh.backend.oauth.payload.SignUpRequest;
import com.leafresh.backend.oauth.security.UserPrincipal;
import com.leafresh.backend.oauth.service.CustomUserDetailsService;
import com.leafresh.backend.oauth.service.TokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

        String accessToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(((UserPrincipal) authentication.getPrincipal()).getUserId());

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken)); // 리프레시 토큰 추가
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

    /**
     * 토큰 유효성 검사 후 리프레시 토큰을 통해 토큰 재생성
     * */
    @PostMapping("/check-token")
    public ResponseEntity<?> validateOrRefreshToken(@RequestHeader("Authorization") String tokenHeader,
                                                    @RequestBody(required = false) Map<String, String> request) {
        String token = tokenHeader.replace("Bearer ", "");

        if (tokenProvider.validateToken(token)) {
            return ResponseEntity.ok().build(); // 액세스 토큰이 유효한 경우
        } else {
            // 액세스 토큰이 만료되었거나 유효하지 않음
            if (request != null && request.containsKey("refreshToken")) {
                String refreshToken = request.get("refreshToken");

                // 리프레시 토큰 유효성 검사 및 액세스 토큰 재발급
                if (tokenProvider.validateRefreshToken(refreshToken)) {
                    try {
                        String newAccessToken = tokenProvider.refreshAccessToken(refreshToken);
                        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new ApiResponse(false, "유효하지 않은 리프레시 토큰입니다."));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ApiResponse(false, "리프레시 토큰이 만료되었습니다. 다시 로그인해주세요."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse(false, "액세스 토큰이 만료되었고, 리프레시 토큰이 제공되지 않았습니다."));
            }
        }
    }
}
