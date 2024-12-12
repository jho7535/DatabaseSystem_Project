package com.sw.club_management_system.controller;

import com.sw.club_management_system.domain.Schedule;
import com.sw.club_management_system.dto.ScheduleRequest;
import com.sw.club_management_system.service.AuthorizationService;
import com.sw.club_management_system.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleViewController {

    private final ScheduleService scheduleService;
    private final AuthorizationService authorizationService;

    // 스케줄 메인 페이지
    @GetMapping
    public String renderSchedulesPage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        List<Schedule> schedules = scheduleService.findAllSchedules();
        model.addAttribute("schedules", schedules);

        return "schedules";
    }

    // 특정 스케줄 상세 페이지
    @GetMapping("/{scheduleId}")
    public String renderScheduleDetailPage(
            @PathVariable Integer scheduleId,
            HttpSession session,
            Model model) {

        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        Schedule schedule = scheduleService.findScheduleById(scheduleId).orElse(null);
        if (schedule == null) {
            model.addAttribute("error", "Schedule not found.");
            return "error"; // error.html
        }

        model.addAttribute("schedule", schedule);

        return "schedule/details"; // schedule/details.html
    }

    // 스케줄 등록 페이지
    @GetMapping("/new")
    public String renderScheduleForm(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        if (!authorizationService.isPresident(session)) {
            model.addAttribute("error", "You do not have permission to create schedules.");
            return "error"; // error.html
        }

        model.addAttribute("schedule", new ScheduleRequest());
        return "schedule/form"; // schedule/form.html
    }

    // 스케줄 생성 처리
    @PostMapping
    public String handleScheduleCreation(
            @ModelAttribute @Valid ScheduleRequest scheduleRequest,
            HttpSession session,
            Model model) {

        // 권한 확인
        if (!authorizationService.isPresident(session)) {
            model.addAttribute("error", "You do not have permission to create schedules.");
            return "error"; // error.html
        }

        boolean isCreated = scheduleService.createSchedule(scheduleRequest, session);

        if (!isCreated) {
            model.addAttribute("error", "Failed to create the schedule. Please try again.");
            return "error"; // error.html
        }

        return "redirect:/schedules"; // 성공 시 일정 목록 페이지로 리다이렉트
    }

    // 스케줄 수정 페이지
    @GetMapping("/{scheduleId}/edit")
    public String renderScheduleEditPage(
            @PathVariable Integer scheduleId,
            HttpSession session,
            Model model) {

        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        if (!authorizationService.isPresidentInSchedule(scheduleId, session)) {
            model.addAttribute("error", "You do not have permission to edit this schedule.");
            return "error"; // error.html
        }

        Schedule schedule = scheduleService.findScheduleById(scheduleId).orElse(null);
        if (schedule == null) {
            model.addAttribute("error", "Schedule not found.");
            return "error"; // error.html
        }

        model.addAttribute("schedule", schedule);
        return "schedule/form"; // schedule/form.html
    }

    // 스케줄 수정 처리
    @PostMapping("/{scheduleId}/edit")
    public String handleScheduleEdit(
            @PathVariable Integer scheduleId,
            @ModelAttribute @Valid ScheduleRequest scheduleRequest,
            HttpSession session,
            Model model) {

        // 권한 확인 (해당 스케줄에 대한 회장 권한이 있는지 체크)
        if (!authorizationService.isPresidentInSchedule(scheduleId, session)) {
            model.addAttribute("error", "You do not have permission to edit this schedule.");
            return "error"; // error.html
        }

        // 수정 처리
        boolean isUpdated = scheduleService.updateSchedule(scheduleId, scheduleRequest);

        if (!isUpdated) {
            model.addAttribute("error", "Failed to update the schedule. Please try again.");
            return "error"; // error.html
        }

        return "redirect:/schedules/" + scheduleId; // 수정 성공 시 상세 페이지로 리다이렉트
    }

    // 일정 참여 처리
    @PostMapping("/participation/{scheduleId}")
    public String participateInSchedule(
            @PathVariable Integer scheduleId,
            HttpSession session,
            Model model) {

        // 스케줄에서 동아리 ID 가져오기
        Integer clubId = scheduleService.findClubIdByScheduleId(scheduleId);

        // 권한 확인
        if (!authorizationService.isMember(clubId, session)) {
            model.addAttribute("error", "You do not have permission to participate in this schedule.");
            return "error"; // error.html
        }

        // 참여 처리
        boolean isParticipated = scheduleService.participateInSchedule(scheduleId,
                (Integer) session.getAttribute("studentNumber"));

        if (!isParticipated) {
            model.addAttribute("error", "Failed to participate in the schedule. Please try again.");
            return "error"; // error.html
        }

        return "redirect:/schedules/" + scheduleId; // 일정 상세 페이지로 리다이렉트
    }

    // 일정 참여 취소 처리
    @PostMapping("/participation/{scheduleId}/cancel")
    public String cancelParticipation(
            @PathVariable Integer scheduleId,
            HttpSession session,
            Model model) {

        // 스케줄에서 동아리 ID 가져오기
        Integer clubId = scheduleService.findClubIdByScheduleId(scheduleId);

        // 권한 확인
        if (!authorizationService.isMember(clubId, session)) {
            model.addAttribute("error", "You do not have permission to cancel participation in this schedule.");
            return "error"; // error.html
        }

        // 참여 취소 처리
        boolean isCanceled = scheduleService.cancelParticipation(scheduleId,
                (Integer) session.getAttribute("studentNumber"));

        if (!isCanceled) {
            model.addAttribute("error", "Failed to cancel participation in the schedule. Please try again.");
            return "error"; // error.html
        }

        return "redirect:/schedules/" + scheduleId; // 일정 상세 페이지로 리다이렉트
    }

}
