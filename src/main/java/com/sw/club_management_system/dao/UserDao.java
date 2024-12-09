package com.sw.club_management_system.dao;

import com.sw.club_management_system.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    // ResultSet 데이터를 User 엔터티로 매핑하는 RowMapper
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setStudentNumber(rs.getInt("student_number")); // 학번 설정
        user.setEmail(rs.getString("email"));              // 이메일 설정
        user.setPassword(rs.getString("password"));        // 비밀번호 설정
        user.setDepartment(rs.getString("department"));    // 학과 설정
        return user;
    };

    // 모든 사용자 조회
    public List<User> findAll() {
        String sql = "SELECT * FROM user"; // SQL 쿼리: 모든 사용자 정보 가져오기
        return jdbcTemplate.query(sql, userRowMapper); // RowMapper를 사용하여 결과 매핑
    }

    // 학번으로 사용자 조회
    public User findById(Integer studentNumber) {
        String sql = "SELECT * FROM user WHERE student_number = ?"; // SQL 쿼리: 학번으로 사용자 찾기
        return jdbcTemplate.queryForObject(sql, userRowMapper, studentNumber); // 사용자 1명 반환
    }

    // 새로운 사용자 추가
    public int insert(User user) {
        String sql = "INSERT INTO user (student_number, email, password, department) VALUES (?, ?, ?, ?)";
        // SQL 쿼리: 사용자 정보 삽입
        return jdbcTemplate.update(
                sql,
                user.getStudentNumber(), // 학번
                user.getEmail(),         // 이메일
                user.getPassword(),      // 비밀번호
                user.getDepartment()     // 학과
        );
    }

    // 사용자 정보 업데이트
    public int update(User user) {
        String sql = "UPDATE user SET email = ?, password = ?, department = ? WHERE student_number = ?";
        // SQL 쿼리: 사용자 정보 수정
        return jdbcTemplate.update(
                sql,
                user.getEmail(),         // 수정할 이메일
                user.getPassword(),      // 수정할 비밀번호
                user.getDepartment(),    // 수정할 학과
                user.getStudentNumber()  // 조건: 학번
        );
    }

    // 학번으로 사용자 삭제
    public int delete(Integer studentNumber) {
        String sql = "DELETE FROM user WHERE student_number = ?"; // SQL 쿼리: 학번으로 사용자 삭제
        return jdbcTemplate.update(sql, studentNumber); // 학번 조건으로 삭제
    }
}
