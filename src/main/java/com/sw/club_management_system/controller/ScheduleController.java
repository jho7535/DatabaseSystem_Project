package com.sw.club_management_system.controller;

import com.sw.club_management_system.domain.Schedule;
import com.sw.club_management_system.dto.ScheduleRequest;
import com.sw.club_management_system.service.AuthorizationService;
import com.sw.club_management_system.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final AuthorizationService authorizationService;

    // 모든 스케줄 조회
    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.findAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    // 특정 클럽의 스케줄 조회
    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<Schedule>> getSchedulesByClubId(@PathVariable Integer clubId) {
        List<Schedule> schedules = scheduleService.findSchedulesByClubId(clubId);
        return ResponseEntity.ok(schedules);
    }

    // 스케줄 생성
    @PostMapping
    public ResponseEntity<String> createSchedule(@RequestBody @Valid ScheduleRequest scheduleRequest, HttpSession session) {
        // 권한 확인 (회장만 가능)
        if (!authorizationService.isPresident(session)) {
            return ResponseEntity.status(403).body("You do not have permission to create schedules.");
        }

        // 서비스 호출
        boolean isCreated = scheduleService.createSchedule(scheduleRequest, session);
        if (!isCreated) {
            return ResponseEntity.badRequest().body("Schedule creation failed.");
        }
        return ResponseEntity.ok("Schedule created successfully.");
    }

    // 학생 스케줄 참여
    @PostMapping("/participation/{scheduleId}")
    public ResponseEntity<String> participateInSchedule(@PathVariable Integer scheduleId, HttpSession session) {
        // 스케줄에서 동아리 ID 가져오기
        Integer clubId = scheduleService.findClubIdByScheduleId(scheduleId);
        // 권한 확인
        if (!authorizationService.isMember(clubId, session)) {
            return ResponseEntity.status(403).body("You do not have permission to participate in schedules.");
        }

        boolean isParticipated = scheduleService.participateInSchedule(scheduleId, (Integer) session.getAttribute("studentNumber"));
        if (!isParticipated) {
            return ResponseEntity.badRequest().body("Participation failed.");
        }
        return ResponseEntity.ok("Participation successful.");
    }

    // 스케줄 수정
    @PutMapping("/{scheduleId}")
    public ResponseEntity<String> updateSchedule(
            @PathVariable Integer scheduleId,
            @RequestBody @Valid ScheduleRequest scheduleRequest,
            HttpSession session) {
        // 권한 확인 (회장만 가능)
        if (!authorizationService.isPresidentInSchedule(scheduleId, session)) {
            return ResponseEntity.status(403).body("You do not have permission to update schedules.");
        }

        boolean isUpdated = scheduleService.updateSchedule(scheduleId, scheduleRequest);
        if (!isUpdated) {
            return ResponseEntity.badRequest().body("Schedule update failed.");
        }
        return ResponseEntity.ok("Schedule updated successfully.");
    }

    // 스케줄 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Integer scheduleId, HttpSession session) {
        // 권한 확인 (회장만 가능)
        if (!authorizationService.isPresidentInSchedule(scheduleId, session)) {
            return ResponseEntity.status(403).body("You do not have permission to delete schedules.");
        }

        boolean isDeleted = scheduleService.deleteSchedule(scheduleId);
        if (!isDeleted) {
            return ResponseEntity.badRequest().body("Schedule deletion failed.");
        }
        return ResponseEntity.ok("Schedule deleted successfully.");
    }

    // 학생 스케줄 참여 취소
    @DeleteMapping("/participation/{scheduleId}")
    public ResponseEntity<String> cancelParticipation(@PathVariable Integer scheduleId, HttpSession session) {
        // 스케줄에서 동아리 ID 가져오기
        Integer clubId = scheduleService.findClubIdByScheduleId(scheduleId);
        // 권한 확인
        if (!authorizationService.isMember(clubId, session)) {
            return ResponseEntity.status(403).body("You do not have permission to cancel participation in schedules.");
        }

        boolean isCanceled = scheduleService.cancelParticipation(scheduleId, (Integer) session.getAttribute("studentNumber"));
        if (!isCanceled) {
            return ResponseEntity.badRequest().body("Participation cancellation failed.");
        }
        return ResponseEntity.ok("Participation canceled successfully.");
    }
}