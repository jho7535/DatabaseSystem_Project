package com.sw.club_management_system.controller;

import com.sw.club_management_system.domain.User;
import com.sw.club_management_system.dto.LoginRequest;
import com.sw.club_management_system.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    // 로그인 페이지 렌더링
    @GetMapping("/login")
    public String renderLoginPage(HttpSession session, Model model) {
        // 세션에서 사용자 정보 확인
        Object email = session.getAttribute("email");

        if (email != null) {
            // 이미 로그인된 상태라면 메시지를 모델에 추가
            model.addAttribute("message", "이미 로그인된 상태입니다.");
            model.addAttribute("email", email);
            return "redirect:/"; // 로그인 상태라면 메인 페이지로 리다이렉트
        }

        // 비로그인 상태라면 로그인 페이지 렌더링
        return "login"; // templates/login.html
    }

    // 회원가입 페이지 렌더링
    @GetMapping("/register")
    public String renderRegisterPage(HttpSession session, Model model) {
        // 세션에서 사용자 정보 확인
        Object email = session.getAttribute("email");

        if (email != null) {
            // 이미 로그인된 상태라면 메시지를 모델에 추가
            model.addAttribute("message", "이미 로그인된 상태입니다.");
            model.addAttribute("email", email);
            return "redirect:/"; // 로그인 상태라면 메인 페이지로 리다이렉트
        }

        // 비로그인 상태라면 회원가입 페이지 렌더링
        return "register"; // templates/register.html
    }

    // 로그인 처리
    @PostMapping("/login")
    public String handleLogin(
            @ModelAttribute LoginRequest loginRequest,
            HttpSession session,
            Model model) {

        // 사용자 인증
        boolean isAuthenticated = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (isAuthenticated) {
            // 인증 성공: 세션에 사용자 정보 저장
            User user = userService.findByEmail(loginRequest.getEmail());
            session.setAttribute("studentNumber", user.getStudentNumber());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("department", user.getDepartment());
            session.setAttribute("role", "user");

            // 메인 페이지로 리다이렉트
            return "redirect:/";
        }

        // 인증 실패: 에러 메시지와 함께 로그인 페이지 다시 렌더링
        model.addAttribute("error", "이메일 또는 비밀번호가 잘못되었습니다.");
        return "login"; // templates/login.html
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String handleRegister(
            @ModelAttribute User user,
            Model model) {

        // 사용자 등록 처리
        boolean isRegistered = userService.register(user);

        if (isRegistered) {
            // 회원가입 성공 시 로그인 페이지로 리다이렉트
            model.addAttribute("successMessage", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/users/login";
        }

        // 회원가입 실패 시 에러 메시지와 함께 회원가입 페이지 렌더링
        model.addAttribute("error", "회원가입에 실패했습니다. 이미 존재하는 이메일일 수 있습니다.");
        return "register"; // templates/register.html
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        // 세션 무효화
        session.invalidate();

        // 메인 페이지로 리다이렉트
        return "redirect:/";
    }
}
