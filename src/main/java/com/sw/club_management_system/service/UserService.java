package com.sw.club_management_system.service;

import com.sw.club_management_system.dao.UserDao;
import com.sw.club_management_system.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    // 회원가입
    public boolean register(User user) {
        if (userDao.findByEmail(user.getEmail()).isPresent() || userDao.findByStudentNumber(user.getStudentNumber()).isPresent()) {
            return false; // 이미 존재하는 사용자
        }
        userDao.insert(user); // 새로운 사용자 추가
        return true;
    }

    // 사용자 인증
    public boolean authenticate(String email, String password) {
        return userDao.findByEmail(email)
                .map(user -> user.getPassword().equals(password)) // 비밀번호 비교
                .orElse(false); // 사용자 존재하지 않으면 false
    }

    // 이메일로 사용자 조회
    public User findByEmail(String email) {
        return userDao.findByEmail(email).orElse(null);
    }
}
