package com.leafresh.backend.oauth.security;

import com.leafresh.backend.oauth.exception.ResourceNotFoundException;
import com.leafresh.backend.oauth.exception.BadRequestException;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.model.Role;
import com.leafresh.backend.oauth.model.UserStatus;
import com.leafresh.backend.oauth.payload.SignUpRequest;
import com.leafresh.backend.oauth.payload.ApiResponse;
import com.leafresh.backend.oauth.repository.UserRepository;
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

    @Transactional
    public ResponseEntity<?> registerUser(@Valid SignUpRequest signUpRequest) {
        if (userRepository.existsByUserMailAdress(signUpRequest.getEmail())) {
            throw new BadRequestException("이미 사용 중인 이메일입니다.");
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
}
