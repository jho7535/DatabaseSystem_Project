package com.sw.club_management_system.dao;

import com.sw.club_management_system.domain.Participation;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ParticipationDao {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper: ResultSet 데이터를 Participation 객체로 매핑
    private final RowMapper<Participation> participationRowMapper = (rs, rowNum) -> {
        Participation participation = new Participation();
        participation.setStudentNumber(rs.getInt("student_number")); // 학생 번호
        participation.setScheduleId(rs.getInt("schedule_id"));       // 일정 ID
        return participation;
    };

    // 모든 참여 데이터 조회
    public List<Participation> findAll() {
        String sql = "SELECT * FROM participation";
        return jdbcTemplate.query(sql, participationRowMapper);
    }

    // 특정 학생이 참여한 모든 일정 조회
    public List<Participation> findByStudentNumber(Integer studentNumber) {
        String sql = "SELECT * FROM participation WHERE student_number = ?";
        return jdbcTemplate.query(sql, participationRowMapper, studentNumber);
    }

    // 특정 일정에 참여한 모든 학생 조회
    public List<Participation> findByScheduleId(Integer scheduleId) {
        String sql = "SELECT * FROM participation WHERE schedule_id = ?";
        return jdbcTemplate.query(sql, participationRowMapper, scheduleId);
    }

    // 특정 학생이 특정 일정에 참여한 데이터 조회
    public Optional<Participation> findByStudentAndSchedule(Integer studentNumber, Integer scheduleId) {
        String sql = "SELECT * FROM participation WHERE student_number = ? AND schedule_id = ?";
        return jdbcTemplate.query(sql, participationRowMapper, studentNumber, scheduleId)
                .stream()
                .findFirst(); // 첫 번째 결과 반환 (Optional)
    }

    // 새로운 참여 데이터 추가
    public int insert(Integer scheduleId, Integer studentNumber) {
        String sql = "INSERT INTO participation (student_number, schedule_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, studentNumber, scheduleId);
    }

    // 특정 학생과 특정 일정의 참여 데이터 삭제
    public int deleteByStudentAndSchedule(Integer studentNumber, Integer scheduleId) {
        String sql = "DELETE FROM participation WHERE student_number = ? AND schedule_id = ?";
        return jdbcTemplate.update(sql, studentNumber, scheduleId);
    }

    // 특정 학생의 모든 참여 데이터 삭제
    public int deleteByStudentNumber(Integer studentNumber) {
        String sql = "DELETE FROM participation WHERE student_number = ?";
        return jdbcTemplate.update(sql, studentNumber);
    }

    // 특정 일정의 모든 참여 데이터 삭제
    public int deleteByScheduleId(Integer scheduleId) {
        String sql = "DELETE FROM participation WHERE schedule_id = ?";
        return jdbcTemplate.update(sql, scheduleId);
    }
}
