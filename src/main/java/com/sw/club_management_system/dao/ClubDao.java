package com.sw.club_management_system.dao;

import com.sw.club_management_system.domain.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClubDao {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper: ResultSet 데이터를 Club 객체로 매핑
    private final RowMapper<Club> clubRowMapper = (rs, rowNum) -> {
        Club club = new Club();
        club.setId(rs.getInt("club_id"));          // 클럽 ID
        club.setClubName(rs.getString("club_name")); // 클럽 이름
        club.setContactInfo(rs.getString("contact_info")); // 연락처
        club.setDescription(rs.getString("description")); // 설명
        club.setSupervisor(rs.getString("supervisor"));   // 지도 교수
        return club;
    };

    // 모든 클럽 조회
    public List<Club> findAll() {
        String sql = "SELECT * FROM club";
        return jdbcTemplate.query(sql, clubRowMapper);
    }

    // 특정 클럽 조회 (ID로 조회)
    public Club findById(Integer id) {
        String sql = "SELECT * FROM club WHERE club_id = ?";
        return jdbcTemplate.queryForObject(sql, clubRowMapper, id);
    }

    // 새로운 클럽 추가
    public int insert(Club club) {
        String sql = "INSERT INTO club (club_name, contact_info, description, supervisor) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                club.getClubName(),   // 클럽 이름
                club.getContactInfo(), // 연락처
                club.getDescription(), // 설명
                club.getSupervisor()   // 지도 교수
        );
    }

    // 클럽 정보 업데이트
    public int update(Club club) {
        String sql = "UPDATE club SET club_name = ?, contact_info = ?, description = ?, supervisor = ? WHERE club_id = ?";
        return jdbcTemplate.update(
                sql,
                club.getClubName(),   // 수정할 클럽 이름
                club.getContactInfo(), // 수정할 연락처
                club.getDescription(), // 수정할 설명
                club.getSupervisor(),  // 수정할 지도 교수
                club.getId()           // 조건: 클럽 ID
        );
    }

    // 클럽 삭제
    public int deleteById(Integer id) {
        String sql = "DELETE FROM club WHERE club_id = ?";
        return jdbcTemplate.update(sql, id); // 조건: 클럽 ID
    }
}
