package com.sw.club_management_system.service;

import com.sw.club_management_system.dao.DocumentDao;
import com.sw.club_management_system.dao.MembershipDao;
import com.sw.club_management_system.domain.Membership;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final MembershipDao membershipDao;
    private final DocumentDao documentDao;

    // 관리자 권한 확인
    public boolean isAdmin(HttpSession session) {
        return "admin".equals(session.getAttribute("role"));
    }

    // 동아리 회장 권한 확인
    public boolean isPresident(HttpSession session) {
        return membershipDao.findByStudentNumber((Integer) session.getAttribute("studentNumber")).stream()
                .anyMatch(membership -> "president".equals(membership.getRole()));
    }

    // 특정 동아리 회장 권한 확인
    public boolean isPresidentInClub(Integer clubId, HttpSession session) {
        String role = membershipDao.findByStudentNumberAndClubId((Integer) session.getAttribute("studentNumber"), clubId)
                .map(Membership::getRole)
                .orElse("");

        return "president".equals(role);
    }

    // 문서에 기반한 동아리 회장 권한 확인
    public boolean isAuthor(Integer documentId, HttpSession session) {
        // 세션에서 학번 가져오기
        Integer studentNumber = (Integer) session.getAttribute("studentNumber");

        if (studentNumber == null) {
            return false; // 학번이 없는 경우 권한 없음
        }

        // DAO에서 문서 정보 가져오기
        return documentDao.findById(documentId)
                .map(document -> {
                    Integer clubId = document.getClubId(); // 문서의 클럽 ID 가져오기

                    // 동아리와 사용자 학번에 기반한 회장 권한 확인
                    return membershipDao.findByStudentNumberAndClubId(studentNumber, clubId)
                            .map(Membership::getRole)
                            .filter("president"::equals) // role이 "president"인지 확인
                            .isPresent();
                })
                .orElse(false); // 문서가 없는 경우 false 반환
    }
}
