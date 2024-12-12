package com.sw.club_management_system.dao;

import com.sw.club_management_system.domain.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleDao {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper: ResultSet 데이터를 Schedule 객체로 매핑
    private final RowMapper<Schedule> scheduleRowMapper = (rs, rowNum) -> {
        Schedule schedule = new Schedule();
        schedule.setId(rs.getInt("schedule_id"));        // 스케줄 ID
        schedule.setClubId(rs.getInt("club_id"));        // 클럽 ID
        schedule.setScheduleName(rs.getString("schedule_name")); // 스케줄 이름
        schedule.setDate(rs.getDate("date").toLocalDate());      // 날짜
        schedule.setLocation(rs.getString("location"));  // 위치
        schedule.setDescription(rs.getString("description")); // 설명
        return schedule;
    };

    // 모든 스케줄 조회
    public List<Schedule> findAll() {
        String sql = "SELECT * FROM schedule";
        return jdbcTemplate.query(sql, scheduleRowMapper);
    }

    // 특정 스케줄 조회 (ID로 조회)
    public Optional<Schedule> findById(Integer id) {
        String sql = "SELECT * FROM schedule WHERE schedule_id = ?";
        return jdbcTemplate.query(sql, scheduleRowMapper, id)
                .stream()
                .findFirst(); // 첫 번째 결과 반환 (Optional)
    }

    // 특정 동아리의 스케줄 조회
    public List<Schedule> findByClubId(Integer clubId) {
        String sql = "SELECT * FROM schedule WHERE club_id = ?";
        return jdbcTemplate.query(sql, scheduleRowMapper, clubId);
    }

    // 새로운 스케줄 추가
    public int insert(Schedule schedule) {
        String sql = "INSERT INTO schedule (club_id, schedule_name, date, location, description) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                schedule.getClubId(),         // 클럽 ID
                schedule.getScheduleName(),   // 스케줄 이름
                schedule.getDate(),           // 날짜
                schedule.getLocation(),       // 위치
                schedule.getDescription()     // 설명
        );
    }

    // 스케줄 정보 업데이트
    public int update(Schedule schedule) {
        String sql = "UPDATE schedule SET club_id = ?, schedule_name = ?, date = ?, location = ?, description = ? WHERE schedule_id = ?";
        return jdbcTemplate.update(
                sql,
                schedule.getClubId(),         // 수정할 클럽 ID
                schedule.getScheduleName(),   // 수정할 스케줄 이름
                schedule.getDate(),           // 수정할 날짜
                schedule.getLocation(),       // 수정할 위치
                schedule.getDescription(),    // 수정할 설명
                schedule.getId()              // 조건: 스케줄 ID
        );
    }

    // 스케줄 삭제
    public int deleteById(Integer id) {
        String sql = "DELETE FROM schedule WHERE schedule_id = ?";
        return jdbcTemplate.update(sql, id); // 조건: 스케줄 ID
    }

    // 특정 클럽의 모든 스케줄 삭제
    public int deleteByClubId(Integer clubId) {
        String sql = "DELETE FROM schedule WHERE club_id = ?";
        return jdbcTemplate.update(sql, clubId); // 조건: 클럽 ID
    }
}
