package com.sw.club_management_system.dao;

import com.sw.club_management_system.domain.DocumentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DocumentStatusDao {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper: ResultSet 데이터를 DocumentStatus 객체로 매핑
    private final RowMapper<DocumentStatus> documentStatusRowMapper = (rs, rowNum) -> {
        DocumentStatus documentStatus = new DocumentStatus();
        documentStatus.setDocumentId(rs.getInt("document_id")); // 문서 ID
        documentStatus.setStatus(rs.getString("status"));     // 상태
        documentStatus.setStatusModifiedDate(
                rs.getTimestamp("status_modified_date").toInstant() // 상태 변경 날짜
        );
        return documentStatus;
    };

    // 모든 문서 상태 조회
    public List<DocumentStatus> findAll() {
        String sql = "SELECT * FROM document_status";
        return jdbcTemplate.query(sql, documentStatusRowMapper);
    }

    // 특정 문서 상태 조회 (문서 ID로 조회)
    public Optional<DocumentStatus> findByDocumentId(Integer documentId) {
        String sql = "SELECT * FROM document_status WHERE document_id = ?";
        return jdbcTemplate.query(sql, documentStatusRowMapper, documentId)
                .stream()
                .findFirst(); // 첫 번째 결과 반환 (Optional)
    }

    // 새로운 문서 상태 추가
    public int insert(DocumentStatus documentStatus) {
        String sql = "INSERT INTO document_status (document_id, status) VALUES (?, ?)";
        return jdbcTemplate.update(
                sql,
                documentStatus.getDocumentId(),         // 문서 ID
                documentStatus.getStatus()         // 상태
        );
    }

    // 문서 상태 업데이트
    public int update(DocumentStatus documentStatus) {
        String sql = "UPDATE document_status SET status = ? WHERE document_id = ?";
        return jdbcTemplate.update(
                sql,
                documentStatus.getStatus(),           // 수정할 상태
                documentStatus.getDocumentId()          // 조건: 문서 ID
        );
    }

    // 문서 상태 삭제
    public int deleteByDocumentId(Integer documentId) {
        String sql = "DELETE FROM document_status WHERE document_id = ?";
        return jdbcTemplate.update(sql, documentId); // 조건: 문서 ID
    }
}
