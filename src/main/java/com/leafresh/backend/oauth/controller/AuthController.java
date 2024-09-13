
package com.leafresh.backend.oauth.controller;

import com.leafresh.backend.oauth.exception.BadRequestException;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.payload.ApiResponse;
import com.leafresh.backend.oauth.payload.AuthResponse;
import com.leafresh.backend.oauth.payload.LoginRequest;
import com.leafresh.backend.oauth.payload.SignUpRequest;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.security.CurrentUser;
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
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository; // UserRepository 주입 추가

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

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUserMailAdress(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "중복된 이메일 입니다"));
        }else if (userRepository.existsByUserNickname(signUpRequest.getNickname())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false,"중복된 닉네임입니다."));
        }else if (!signUpRequest.isTermsAgreement()) {
            throw new BadRequestException("약관에 동의해야 합니다.");
        }


        return customUserDetailsService.registerUser(signUpRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiResponse(true, "사용자가 성공적으로 로그아웃되었습니다."));
    }

    @PostMapping("/check-token")
    public ResponseEntity<?> validateOrRefreshToken(@RequestHeader("Authorization") String tokenHeader,
                                                    @RequestBody(required = false) Map<String, String> request) {
        String token = tokenHeader.replace("Bearer ", "");

        if (tokenProvider.validateToken(token)) {
            return ResponseEntity.ok().build();
        } else {
            if (request != null && request.containsKey("refreshToken")) {
                String refreshToken = request.get("refreshToken");

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
