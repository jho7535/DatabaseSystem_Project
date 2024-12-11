package com.sw.club_management_system.controller;

import com.sw.club_management_system.domain.Document;
import com.sw.club_management_system.service.AuthorizationService;
import com.sw.club_management_system.service.DocumentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final AuthorizationService authorizationService;

    // 모든 문서 조회
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments(HttpSession session) {
        // 권한 확인
        if (!authorizationService.isAdmin(session) && !authorizationService.isPresident(session)) {
            return ResponseEntity.status(403).body(null);
        }
        List<Document> documents = documentService.findAll();
        return ResponseEntity.ok(documents);
    }

    // 특정 문서 조회
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Integer id, HttpSession session) {
        // 권한 확인
        if (!authorizationService.isAdmin(session) && !authorizationService.isAuthor(id, session)) {
            return ResponseEntity.status(403).body(null);
        }

        return documentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 특정 동아리의 문서 조회
    @GetMapping("/clubs/{clubId}")
    public ResponseEntity<List<Document>> getDocumentsByClubId(@PathVariable Integer clubId, HttpSession session) {
        // 권한 확인
        if (!authorizationService.isAdmin(session) && !authorizationService.isPresidentInClub(clubId, session)) {
            return ResponseEntity.status(403).body(null);
        }

        List<Document> documents = documentService.findByClubId(clubId);
        return ResponseEntity.ok(documents);
    }

    // 문서 등록
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> registerDocument(
            @RequestPart("file") MultipartFile file,         // 업로드할 파일
            @RequestPart("documentName") String documentName, // 문서 이름
            @RequestPart("clubName") String clubName, // 동아리 이름
            HttpSession session) {

        // 권한 확인
        if (!authorizationService.isPresident(session)) {
            return ResponseEntity.status(403).body("You do not have permission to register documents.");
        }

        boolean isRegistered = documentService.registerDocument(file, documentName, clubName);
        if (!isRegistered) {
            return ResponseEntity.badRequest().body("Document registration failed.");
        }

        return ResponseEntity.ok("Document registered successfully.");
    }

    // 문서 상태 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDocumentStatus(@PathVariable Integer id, @RequestParam String status, HttpSession session) {
        // 권한 확인
        if (!authorizationService.isAdmin(session)) {
            return ResponseEntity.status(403).body(null);
        }

        boolean isUpdated = documentService.updateDocumentStatus(id, status);
        if (!isUpdated) {
            return ResponseEntity.badRequest().body("Document status update failed.");
        }
        return ResponseEntity.ok("Document status updated successfully.");
    }

    // 문서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Integer id, HttpSession session) {
        // 권한 확인
        if (!authorizationService.isAdmin(session) && !authorizationService.isAuthor(id, session)) {
            return ResponseEntity.status(403).body(null);
        }

        boolean isDeleted = documentService.deleteDocument(id);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Document deleted successfully.");
    }
}