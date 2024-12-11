package com.sw.club_management_system.service;

import com.sw.club_management_system.dao.ClubDao;
import com.sw.club_management_system.dao.DocumentDao;
import com.sw.club_management_system.dao.DocumentStatusDao;
import com.sw.club_management_system.domain.Club;
import com.sw.club_management_system.domain.Document;
import com.sw.club_management_system.domain.DocumentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentDao documentDao;
    private final DocumentStatusDao documentStatusDao;
    private final ClubDao clubDao;

    @Value("${file.upload-dir}")
    private String uploadDir; // 파일 저장 디렉터리

    // 모든 문서 조회
    public List<Document> findAll() {
        return documentDao.findAll();
    }

    // 특정 문서 조회
    public Optional<Document> findById(Integer id) {
        return documentDao.findById(id);
    }

    // 특정 동아리의 문서 조회
    public List<Document> findByClubId(Integer clubId) {
        return documentDao.findByClubId(clubId);
    }

    // 문서 등록
    public boolean registerDocument(MultipartFile file, String documentName, String clubName) {
        try {
            // 디렉터리 생성 (존재하지 않을 경우)
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 동아리 이름으로 동아리 ID 조회
            Integer clubId = clubDao.findByName(clubName)
                    .map(Club::getId)
                    .orElse(null);
            if (clubId == null) {
                return false; // 동아리가 없으면 false 반환
            }

            // 파일 저장
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + uniqueFileName;
            File dest = new File(filePath);
            file.transferTo(dest);

            // 문서 엔터티 생성
            Document document = new Document();
            document.setDocumentName(documentName);
            document.setFile(filePath);
            document.setClubId(clubId);
            document.setSubmissionDate(Instant.now());

            // 문서 등록
            int rowsInserted = documentDao.insert(document);
            if (rowsInserted > 0) {
                // 문서 상태 초기화
                DocumentStatus status = new DocumentStatus();
                status.setDocumentId(document.getId());
                status.setStatus("Submitted");
                status.setStatusModifiedDate(Instant.now());
                documentStatusDao.insert(status);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 문서 상태 업데이트
    public boolean updateDocumentStatus(Integer documentId, String status) {
        // 문서 상태 조회
        return documentStatusDao.findByDocumentId(documentId)
                .map(documentStatus -> {
                    // 문서 상태 업데이트
                    documentStatus.setStatus(status);
                    documentStatus.setStatusModifiedDate(Instant.now()); // 상태 변경 시간 업데이트
                    return documentStatusDao.update(documentStatus); // 업데이트 실행
                })
                .orElse(0) > 0; // 문서 상태가 없으면 false 반환
    }

    // 문서 삭제
    public boolean deleteDocument(Integer id) {
        // 1. 문서 조회 (파일 경로 가져오기)
        Optional<Document> documentOptional = documentDao.findById(id);
        if (documentOptional.isEmpty()) {
            return false; // 문서가 존재하지 않음
        }

        Document document = documentOptional.get();

        // 2. 파일 삭제
        String filePath = document.getFile();
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists() && !file.delete()) {
                System.err.println("Failed to delete file: " + filePath);
            }
        }

        // 3. 문서 상태 삭제
        documentStatusDao.deleteByDocumentId(id);

        // 4. 문서 삭제
        return documentDao.deleteById(id) > 0;
    }
}
