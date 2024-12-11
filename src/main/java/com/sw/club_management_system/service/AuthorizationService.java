package com.sw.club_management_system.service;

import com.sw.club_management_system.dao.MembershipDao;
import com.sw.club_management_system.domain.Membership;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final MembershipDao membershipDao;

    // 관리자 권한 확인
    public boolean isAdmin(HttpSession session) {
        return "admin".equals(session.getAttribute("role"));
    }

    // 회장 권한 확인
    public boolean isPresident(Integer clubId, HttpSession session) {
        String role = membershipDao.findByStudentNumberAndClubId((Integer) session.getAttribute("studentNumber"), clubId)
                .map(Membership::getRole)
                .orElse("");

        return "president".equals(role);
    }
}
