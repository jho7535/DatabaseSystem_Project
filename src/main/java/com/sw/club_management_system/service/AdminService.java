package com.sw.club_management_system.service;

import com.sw.club_management_system.dao.AdminDao;
import com.sw.club_management_system.domain.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminDao adminDao;

    // 관리자 인증
    public boolean authenticate(String email, String password) {
        return adminDao.findByEmail(email)
                .map(admin -> admin.getPassword().equals(password)) // 비밀번호 비교
                .orElse(false); // 관리자 계정이 없으면 false
    }

    // 이메일로 관리자 조회
    public Admin findByEmail(String email) {
        return adminDao.findByEmail(email).orElse(null);
    }
}
