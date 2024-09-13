package com.leafresh.backend.oauth.service;

import com.leafresh.backend.oauth.exception.ResourceNotFoundException;
import com.leafresh.backend.oauth.exception.BadRequestException;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.model.Role;
import com.leafresh.backend.oauth.model.UserStatus;
import com.leafresh.backend.oauth.payload.SignUpRequest;
import com.leafresh.backend.oauth.payload.ApiResponse;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUserMailAdress(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );


        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", id)
                );

        return UserPrincipal.create(user);
    }

    // 새로운 메서드 추가: User 엔티티 직접 반환
    @Transactional
    public User loadUserEntityById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Transactional
    public ResponseEntity<?> registerUser(@Valid SignUpRequest signUpRequest) {
        if (userRepository.existsByUserMailAdress(signUpRequest.getEmail())) {
            throw new BadRequestException("이미 사용 중인 이메일입니다.");
        }else if (userRepository.existsByUserNickname(signUpRequest.getNickname())) {
            throw new BadRequestException("이미 사용 중인 닉네임입니다.");
        }

        try {
            User user = new User();
            user.setUserName(signUpRequest.getName());
            user.setUserNickname(signUpRequest.getNickname());
            user.setUserPhoneNumber(signUpRequest.getPhoneNumber());
            user.setUserMailAdress(signUpRequest.getEmail());
            user.setUserPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            user.setUserJoinDate(new Date());
            user.setUserReportCount(0);
            user.setUserStatus(signUpRequest.isTermsAgreement() ? UserStatus.ACTIVE : UserStatus.SUSPENDED);
            user.setRole(Role.USER);
            user.setTermsAgreement(signUpRequest.isTermsAgreement());

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

    // 약관 동의 상태 업데이트 메서드 추가
    @Transactional
    public User updateTermsAgreement(Integer userId, boolean termsAgreement) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setTermsAgreement(termsAgreement);
        if (!termsAgreement) {
            user.setUserStatus(UserStatus.SUSPENDED);
        }
        return userRepository.save(user);
    }


    // 비밀번호 검증 메서드 추가
    @Transactional
    public boolean verifyPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getUserPassword());
    }

    // 사용자 정보 업데이트 메서드 수정
    @Transactional
    public User updateUser(Integer userId, Map<String, String> updates) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (updates.containsKey("newNickname")) {
            String newNickname = updates.get("newNickname");
            if (newNickname != null && !newNickname.trim().isEmpty()) {
                user.setUserNickname(newNickname);
            }
        }

        if (updates.containsKey("newPassword")) {
            String newPassword = updates.get("newPassword");
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                user.setUserPassword(passwordEncoder.encode(newPassword));
            }
        }

        if (updates.containsKey("newImageUrl")) {
            String newImageUrl = updates.get("newImageUrl");
            user.setImageUrl(newImageUrl);
        }

        return userRepository.save(user);
    }
}
