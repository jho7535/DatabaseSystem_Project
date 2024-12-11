package com.sw.club_management_system.dao;

import com.sw.club_management_system.domain.Document;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DocumentDao {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper: ResultSet 데이터를 Document 객체로 매핑
    private final RowMapper<Document> documentRowMapper = (rs, rowNum) -> {
        Document document = new Document();
        document.setId(rs.getInt("document_id"));          // 문서 ID
        document.setDocumentName(rs.getString("document_name")); // 문서 이름
        document.setClubId(rs.getInt("club_id"));            // 클럽 ID
        document.setFile(rs.getString("file"));             // 파일 데이터
        document.setSubmissionDate(rs.getTimestamp("submission_date").toInstant()); // 제출 날짜
        return document;
    };

    // 모든 문서 조회
    public List<Document> findAll() {
        String sql = "SELECT * FROM document";
        return jdbcTemplate.query(sql, documentRowMapper);
    }

    // 특정 문서 조회 (ID로 조회)
    public Optional<Document> findById(Integer id) {
        String sql = "SELECT * FROM document WHERE document_id = ?";
        return jdbcTemplate.query(sql, documentRowMapper, id)
                .stream()
                .findFirst(); // 첫 번째 결과 반환 (Optional)
    }

    // 특정 동아리의 문서 조회
    public List<Document> findByClubId(Integer clubId) {
        String sql = "SELECT * FROM document WHERE club_id = ?";
        return jdbcTemplate.query(sql, documentRowMapper, clubId);
    }

    // 새로운 문서 추가
    public int insert(Document document) {
        String sql = "INSERT INTO document (document_name, club_id, file, submission_date) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, document.getDocumentName());
            ps.setInt(2, document.getClubId()); // 동아리 ID 추가
            ps.setString(3, document.getFile());
            ps.setTimestamp(4, Timestamp.from(document.getSubmissionDate()));
            return ps;
        }, keyHolder);

        // 자동 생성된 ID를 설정
        Number key = keyHolder.getKey();
        if (key != null) {
            document.setId(key.intValue());
        }
        return keyHolder.getKey() != null ? 1 : 0;
    }

    // 문서 정보 업데이트
    public int update(Document document) {
        String sql = "UPDATE document SET document_name = ?, club_id = ?, file = ? WHERE document_id = ?";
        return jdbcTemplate.update(
                sql,
                document.getDocumentName(),  // 수정할 문서 이름
                document.getClubId(),          // 수정할 클럽 ID
                document.getFile(),          // 수정할 파일 데이터
                document.getId()             // 조건: 문서 ID
        );
    }

    // 문서 삭제
    public int deleteById(Integer id) {
        String sql = "DELETE FROM document WHERE document_id = ?";
        return jdbcTemplate.update(sql, id); // 조건: 문서 ID
    }

}
