package com.sw.club_management_system.dao;

import com.sw.club_management_system.domain.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminDao {

    private final JdbcTemplate jdbcTemplate;

    // ResultSet 데이터를 Admin 엔터티로 매핑하는 RowMapper
    private final RowMapper<Admin> adminRowMapper = (rs, rowNum) -> {
        Admin admin = new Admin();
        admin.setEmail(rs.getString("email"));      // 이메일 설정
        admin.setPassword(rs.getString("password")); // 비밀번호 설정
        return admin;
    };

    // 이메일로 관리자 조회
    public Admin findByEmail(String email) {
        String sql = "SELECT * FROM admin WHERE email = ?"; // SQL 쿼리: 이메일로 관리자 찾기
        return jdbcTemplate.queryForObject(sql, adminRowMapper, email); // 관리자 1명 반환
    }

    // 새로운 관리자 추가
    public int insert(Admin admin) {
        String sql = "INSERT INTO admin (email, password) VALUES (?, ?)";
        return jdbcTemplate.update(
                sql,
                admin.getEmail(),    // 이메일
                admin.getPassword()  // 비밀번호
        );
    }

    // 관리자 비밀번호 업데이트
    public int updatePassword(String email, String newPassword) {
        String sql = "UPDATE admin SET password = ? WHERE email = ?";
        return jdbcTemplate.update(sql, newPassword, email);
    }

    // 관리자 삭제
    public int deleteByEmail(String email) {
        String sql = "DELETE FROM admin WHERE email = ?";
        return jdbcTemplate.update(sql, email);
    }
}
