package com.sw.club_management_system.controller;

import com.sw.club_management_system.dto.LoginRequest;
import com.sw.club_management_system.domain.Admin;
import com.sw.club_management_system.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest, HttpSession session) {
        boolean isAuthenticated = adminService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        if (isAuthenticated) {
            Admin admin = adminService.findByEmail(loginRequest.getEmail());

            // 세션에 관리자 정보 저장
            session.setAttribute("email", admin.getEmail());
            session.setAttribute("role", "admin");

            return ResponseEntity.ok("Admin login successful.");
        }
        return ResponseEntity.status(401).body("Invalid email or password.");
    }
}
