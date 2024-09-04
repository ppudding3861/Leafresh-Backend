package com.leafresh.chat.controller;

import com.leafresh.chat.model.entity.User;
import com.leafresh.chat.service.UserService;
import com.leafresh.chat.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatLoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.loginUser(username, password);
        if (user != null) {
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(token); // 클라이언트에게 JWT 토큰을 반환
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        userService.registerUser(username, password);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout() {
        // 클라이언트 측에서 JWT 토큰을 삭제하여 로그아웃을 처리
        return "redirect:/login";
    }
}
