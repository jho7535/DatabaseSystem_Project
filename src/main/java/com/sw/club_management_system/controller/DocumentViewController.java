package com.sw.club_management_system.controller;

import com.sw.club_management_system.domain.Document;
import com.sw.club_management_system.domain.DocumentStatus;
import com.sw.club_management_system.service.AuthorizationService;
import com.sw.club_management_system.service.DocumentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentViewController {

    private final DocumentService documentService;
    private final AuthorizationService authorizationService;

    // 문서 메인 페이지
    @GetMapping
    public String renderDocumentsPage(HttpSession session, Model model) {
        // 세션에서 값 가져오기
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        // 로그인 여부와 사용자 ID를 모델에 추가
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        boolean isAdmin = authorizationService.isAdmin(session);

        // 권한 확인
        if (!isAdmin && !authorizationService.isPresident(session)) {
            model.addAttribute("error", "You do not have permission to view documents.");
            return "error"; // error.html
        }

        // 문서 목록 가져오기
        List<Document> documents = documentService.findAll();
        model.addAttribute("documents", documents);

        return "documents"; // documents.html
    }

    // 문서 상세 페이지
    @GetMapping("/{id}")
    public String renderDocumentDetailPage(@PathVariable Integer id, HttpSession session, Model model) {
        // 세션에서 값 가져오기
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        // 로그인 여부와 사용자 ID를 모델에 추가
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        // 권한 확인
        if (!authorizationService.isAdmin(session) && !authorizationService.isAuthor(id, session)) {
            model.addAttribute("error", "You do not have permission to view this document.");
            return "error"; // error.html
        }

        // 문서 정보 가져오기
        Document document = documentService.findById(id).orElse(null);
        if (document == null) {
            model.addAttribute("error", "Document not found.");
            return "error"; // error.html
        }

        model.addAttribute("document", document);

        // 문서 상태 정보 가져오기
        DocumentStatus documentStatus = documentService.findStatusByDocumentId(id).orElse(null);
        model.addAttribute("status", documentStatus);

        return "document/details";
    }

    // 문서 등록 페이지
    @GetMapping("/new")
    public String renderDocumentRegisterPage(HttpSession session, Model model) {
        // 세션에서 값 가져오기
        String userId = (String) session.getAttribute("email");
        boolean isLoggedIn = (userId != null);

        // 로그인 여부와 사용자 ID를 모델에 추가
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("userId", userId);

        // 권한 확인
        if (!authorizationService.isPresident(session)) {
            model.addAttribute("error", "You do not have permission to register documents.");
            return "error"; // error.html
        }

        return "document/form";
    }

    // 문서 등록 처리
    @PostMapping
    public String handleDocumentRegistration(
            @RequestParam("documentName") String documentName,
            @RequestParam("clubName") String clubName,
            @RequestParam("file") MultipartFile file,
            HttpSession session,
            Model model) {

        // 권한 확인
        if (!authorizationService.isPresident(session)) {
            model.addAttribute("error", "You do not have permission to register documents.");
            return "redirect:/error"; // error.html
        }

        // 문서 등록 처리
        boolean isRegistered = documentService.registerDocument(file, documentName, clubName);
        if (!isRegistered) {
            model.addAttribute("error", "Document registration failed. Ensure the club name is correct.");
            return "redirect:/error"; // error.html
        }

        return "redirect:/documents"; // 성공 시 문서 목록 페이지로 리다이렉트
    }

//    // 문서 수정 페이지
//    @GetMapping("/{id}/edit")
//    public String renderDocumentEditPage(@PathVariable Integer id, HttpSession session, Model model) {
//        String userId = (String) session.getAttribute("email");
//        boolean isLoggedIn = (userId != null);
//
//        model.addAttribute("isLoggedIn", isLoggedIn);
//        model.addAttribute("userId", userId);
//
//        if (!authorizationService.isAdmin(session) && !authorizationService.isAuthor(id, session)) {
//            model.addAttribute("error", "You do not have permission to edit this document.");
//            return "error";
//        }
//
//        Document document = documentService.findById(id).orElse(null);
//        if (document == null) {
//            model.addAttribute("error", "Document not found.");
//            return "error";
//        }
//
//        model.addAttribute("document", document);
//
//        return "document/from";
//    }

    // 문서 삭제 처리
    @PostMapping("/{id}/delete")
    public String handleDocumentDeletion(
            @PathVariable Integer id,
            HttpSession session,
            Model model) {

        // 권한 확인
        if (!authorizationService.isAdmin(session) && !authorizationService.isAuthor(id, session)) {
            model.addAttribute("error", "You do not have permission to delete this document.");
            return "redirect:/error"; // error.html
        }

        // 문서 삭제 처리
        boolean isDeleted = documentService.deleteDocument(id);
        if (!isDeleted) {
            model.addAttribute("error", "Document not found or could not be deleted.");
            return "redirect:/error"; // error.html
        }

        // 성공 시 문서 목록 페이지로 리다이렉트
        return "redirect:/documents";
    }

    // 문서 상태 변경 처리
    @PostMapping("/{id}/status/toggle")
    public String toggleDocumentStatus(
            @PathVariable Integer id,
            HttpSession session,
            Model model) {

        // 권한 확인 (관리자만 가능)
        if (!authorizationService.isAdmin(session)) {
            model.addAttribute("error", "You do not have permission to change the document status.");
            return "error"; // error.html
        }

        // 현재 상태 조회
        DocumentStatus status = documentService.findStatusByDocumentId(id).orElse(null);
        if (status == null) {
            model.addAttribute("error", "Document status not found.");
            return "error"; // error.html
        }

        // 상태 반전 처리
        String newStatus = status.getStatus().equals("Submitted") ? "Rejected" : "Submitted";
        boolean isUpdated = documentService.updateDocumentStatus(id, newStatus);

        if (!isUpdated) {
            model.addAttribute("error", "Failed to update the document status.");
            return "error"; // error.html
        }

        return "redirect:/documents/" + id; // 성공 시 문서 상세 페이지로 리다이렉트
    }
}
