package com.sw.club_management_system.controller;

import com.sw.club_management_system.domain.Admin;
import com.sw.club_management_system.dto.LoginRequest;
import com.sw.club_management_system.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminViewController {

    private final AdminService adminService;

    // 로그인 처리
    @PostMapping("/login")
    public String handleLogin(
            @ModelAttribute LoginRequest loginRequest,
            HttpSession session,
            Model model) {

        // 로그인 시도
        boolean isAuthenticated = adminService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (isAuthenticated) {
            Admin admin = adminService.findByEmail(loginRequest.getEmail());

            // 세션에 관리자 정보 저장
            session.setAttribute("email", admin.getEmail());
            session.setAttribute("role", "admin");

            return "redirect:/"; // 로그인 성공: 메인 페이지로 리다이렉트
        }

        // 인증 실패: 에러 메시지와 함께 로그인 페이지 다시 렌더링
        model.addAttribute("error", "이메일 또는 비밀번호가 잘못되었습니다.");
        return "login"; // templates/login.html
    }
}
