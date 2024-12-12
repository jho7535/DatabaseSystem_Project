package com.sw.club_management_system.service;

import com.sw.club_management_system.dao.MembershipDao;
import com.sw.club_management_system.dao.ParticipationDao;
import com.sw.club_management_system.dao.ScheduleDao;
import com.sw.club_management_system.domain.Membership;
import com.sw.club_management_system.domain.Schedule;
import com.sw.club_management_system.dto.ScheduleRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleDao scheduleDao;
    private final ParticipationDao participationDao;
    private final MembershipDao membershipDao;

    // 모든 스케줄 조회
    public List<Schedule> findAllSchedules() {
        return scheduleDao.findAll();
    }

    // 특정 클럽의 스케줄 조회
    public List<Schedule> findSchedulesByClubId(Integer clubId) {
        return scheduleDao.findByClubId(clubId);
    }

    // 특정 일정의 동아리 ID 조회
    public Integer findClubIdByScheduleId(Integer scheduleId) {
        return scheduleDao.findById(scheduleId)
                .map(Schedule::getClubId)
                .orElse(null);
    }

    // 특정 일정 조회
    public Optional<Schedule> findScheduleById(Integer scheduleId) {
        return scheduleDao.findById(scheduleId);
    }

    // 스케줄 생성
    public boolean createSchedule(ScheduleRequest scheduleRequest, HttpSession session) {
        Integer clubId = membershipDao.findByStudentNumber((Integer) session.getAttribute("studentNumber"))
                .stream()
                .filter(membership -> "president".equals(membership.getRole()))
                .map(Membership::getClubId)
                .findFirst()
                .orElse(null);

        Schedule schedule = new Schedule();
        schedule.setClubId(clubId);
        schedule.setScheduleName(scheduleRequest.getScheduleName());
        schedule.setDate(scheduleRequest.getDate());
        schedule.setLocation(scheduleRequest.getLocation());
        schedule.setDescription(scheduleRequest.getDescription());

        return scheduleDao.insert(schedule) > 0;
    }

    // 학생 스케줄 참여
    public boolean participateInSchedule(Integer scheduleId, Integer studentNumber) {
        return participationDao.insert(scheduleId, studentNumber) > 0;
    }

    // 스케줄 수정
    public boolean updateSchedule(Integer scheduleId, ScheduleRequest scheduleRequest) {
        Schedule schedule = scheduleDao.findById(scheduleId)
                .orElse(null);

        if (schedule == null) {
            return false;
        }

        schedule.setScheduleName(scheduleRequest.getScheduleName());
        schedule.setDate(scheduleRequest.getDate());
        schedule.setLocation(scheduleRequest.getLocation());
        schedule.setDescription(scheduleRequest.getDescription());

        return scheduleDao.update(schedule) > 0;
    }

    // 스케줄 삭제
    public boolean deleteSchedule(Integer scheduleId) {
        participationDao.deleteByScheduleId(scheduleId); // 참여 데이터 삭제
        return scheduleDao.deleteById(scheduleId) > 0;
    }

    // 학생 스케줄 참여 취소
    public boolean cancelParticipation(Integer scheduleId, Integer studentNumber) {
        return participationDao.deleteByStudentAndSchedule(studentNumber, scheduleId) > 0;
    }
}
