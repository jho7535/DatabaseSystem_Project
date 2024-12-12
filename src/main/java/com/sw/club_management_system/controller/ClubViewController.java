package com.sw.club_management_system.controller;

import com.sw.club_management_system.domain.Club;
import com.sw.club_management_system.domain.User;
import com.sw.club_management_system.service.AuthorizationService;
import com.sw.club_management_system.service.ClubService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubViewController {

    private final ClubService clubService;
    private final AuthorizationService authorizationService;

    // 동아리 목록 페이지
    @GetMapping
    public String listClubs(Model model, HttpSession session) {
        // 세션에서 값 가져오기
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        // 로그인 여부와 사용자 ID를 모델에 추가
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        List<Club> clubs = clubService.findAll();
        model.addAttribute("clubs", clubs);
        return "clubs"; // clubs.html
    }

    // 동아리 상세 페이지
    @GetMapping("/{id}")
    public String clubDetails(@PathVariable Integer id, Model model, HttpSession session) {
        // 세션에서 값 가져오기
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        // 로그인 여부와 사용자 ID를 모델에 추가
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        clubService.findById(id).ifPresentOrElse(
                club -> model.addAttribute("club", club),
                () -> model.addAttribute("redirect:/error", "Club not found.")
        );
        return "club/details"; // club/details.html
    }

    // 동아리 생성 폼
    @GetMapping("/new")
    public String createClubForm(HttpSession session, Model model) {
        // 세션에서 값 가져오기
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        // 로그인 여부와 사용자 ID를 모델에 추가
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        if (!authorizationService.isAdmin(session)) {
            model.addAttribute("error", "Access denied.");
            return "redirect:/error"; // error.html
        }
        model.addAttribute("club", new Club());
        return "club/form"; // club/form.html
    }

    // 동아리 생성 처리
    @PostMapping
    public String createClub(@ModelAttribute Club club, HttpSession session, Model model) {
        // 세션에서 값 가져오기
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        // 로그인 여부와 사용자 ID를 모델에 추가
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        if (!authorizationService.isAdmin(session)) {
            model.addAttribute("error", "Access denied.");
            return "redirect:/error"; // error.html
        }
        clubService.create(club);
        return "redirect:/clubs";
    }

    // 동아리 수정 폼
    @GetMapping("/{id}/edit")
    public String editClubForm(@PathVariable Integer id, HttpSession session, Model model) {
        // 세션에서 값 가져오기
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        // 로그인 여부와 사용자 ID를 모델에 추가
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        if (!authorizationService.isAdmin(session) && !authorizationService.isPresidentInClub(id, session)) {
            model.addAttribute("error", "Access denied.");
            return "redirect:/error"; // error.html
        }
        clubService.findById(id).ifPresentOrElse(
                club -> model.addAttribute("club", club),
                () -> model.addAttribute("error", "Club not found.")
        );
        return "club/form"; // club/form.html
    }

    // 동아리 수정 처리
    @PostMapping("/{id}/edit")
    public String updateClub(@PathVariable Integer id, @ModelAttribute Club club, HttpSession session, Model model) {
        if (!authorizationService.isAdmin(session) && !authorizationService.isPresidentInClub(id, session)) {
            model.addAttribute("error", "Access denied.");
            return "redirect:/error"; // error.html
        }
        boolean isUpdated = clubService.update(id, club);
        if (!isUpdated) {
            model.addAttribute("error", "Club not found.");
            return "redirect:/error"; // error.html
        }
        return "redirect:/clubs/" + id;
    }

    // 동아리 삭제
    @PostMapping("/{id}/delete")
    public String deleteClub(@PathVariable Integer id, HttpSession session, Model model) {
        if (!authorizationService.isAdmin(session)) {
            model.addAttribute("error", "Access denied.");
            return "redirect:/error"; // error.html
        }
        boolean isDeleted = clubService.delete(id);
        if (!isDeleted) {
            model.addAttribute("error", "Club not found.");
            return "redirect:/error"; // error.html
        }
        return "redirect:/clubs";
    }

    // 동아리 모든 멤버 조회
    @GetMapping("/{id}/members")
    public String viewMembersByClubId(@PathVariable Integer id, HttpSession session, Model model) {
        // 세션에서 값 가져오기
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        // 로그인 여부와 사용자 ID를 모델에 추가
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        // 권한 확인
        if (!authorizationService.isAdmin(session) && !authorizationService.isPresidentInClub(id, session)) {
            model.addAttribute("error", "접근 권한이 없습니다.");
            return "redirect:/error"; // error.html
        }

        // 동아리 멤버 조회
        List<User> members = clubService.getMembersByClubId(id);
        model.addAttribute("members", members);

        // 동아리 이름 조회 (옵션: 더 나은 사용자 경험 제공)
        Club club = clubService.findById(id).orElse(null);
        model.addAttribute("clubName", club != null ? club.getClubName() : "알 수 없음");

        return "club/members"; // club/members.html
    }
}
