package com.leafresh.backend.oauth.controller;

import com.leafresh.backend.oauth.exception.ResourceNotFoundException;
import com.leafresh.backend.oauth.model.User;
import com.leafresh.backend.oauth.repository.UserRepository;
import com.leafresh.backend.oauth.security.CurrentUser;
import com.leafresh.backend.oauth.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        // userPrincipal에서 userId를 가져올 때 적절한 메서드 사용
        Integer userId = userPrincipal.getUserId();

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}
