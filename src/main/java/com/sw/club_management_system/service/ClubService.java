package com.sw.club_management_system.service;

import com.sw.club_management_system.dao.ClubDao;
import com.sw.club_management_system.dao.MembershipDao;
import com.sw.club_management_system.dao.ScheduleDao;
import com.sw.club_management_system.domain.Club;
import com.sw.club_management_system.domain.Membership;
import com.sw.club_management_system.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubDao clubDao;
    private final MembershipDao membershipDao;
    private final ScheduleDao scheduleDao;

    public List<Club> findAll() {
        return clubDao.findAll();
    }

    public Optional<Club> findById(Integer id) {
        return clubDao.findById(id);
    }

    // 학번과 클럽 ID를 기반으로 멤버십 정보 반환
    public Optional<Membership> findMembership(Integer studentNumber, Integer clubId) {
        return membershipDao.findByStudentNumberAndClubId(studentNumber, clubId);
    }

    // 동아리 모든 멤버 조회
    public List<User> getMembersByClubId(Integer clubId) {
        return membershipDao.findMembersByClubId(clubId);
    }

    // 동아리 모든 멤버십 조회
    public List<Membership> getMembershipByClubId(Integer clubId) {
        return membershipDao.findByClubId(clubId);
    }

    public void create(Club club) {
        clubDao.insert(club);
    }

    public boolean update(Integer id, Club club) {
        if (clubDao.findById(id).isEmpty()) {
            return false;
        }
        club.setId(id);
        clubDao.update(club);
        return true;
    }

    public boolean delete(Integer id) {
        // 연관된 멤버십 삭제
        membershipDao.deleteByClubId(id);

        // 연관된 스케줄 삭제
        scheduleDao.deleteByClubId(id);

        if (clubDao.findById(id).isEmpty()) {
            return false;
        }
        clubDao.deleteById(id);
        return true;
    }
}
