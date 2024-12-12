package com.sw.club_management_system.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String renderIndex(HttpSession session, Model model) {
        // 세션에서 값 가져오기
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        // 로그인 여부와 사용자 ID를 모델에 추가
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        // index.html 반환
        return "index";
    }
}
