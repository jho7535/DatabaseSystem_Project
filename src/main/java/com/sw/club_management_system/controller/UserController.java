package com.sw.club_management_system.controller;

import com.sw.club_management_system.domain.User;
import com.sw.club_management_system.dto.LoginRequest;
import com.sw.club_management_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid User user) {
        boolean isRegistered = userService.register(user);
        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully.");
        }
        return ResponseEntity.badRequest().body("User registration failed. User may already exist.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest, HttpSession session) {
        boolean isAuthenticated = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        if (isAuthenticated) {
            User user = userService.findByEmail(loginRequest.getEmail());

            // 세션에 사용자 정보 저장
            session.setAttribute("studentNumber", user.getStudentNumber());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("department", user.getDepartment());
            session.setAttribute("role", "user");

            return ResponseEntity.ok("Login successful.");
        }
        return ResponseEntity.status(401).body("Invalid email or password.");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok("Logged out successfully.");
    }
}
