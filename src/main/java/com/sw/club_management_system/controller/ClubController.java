package com.sw.club_management_system.controller;

import com.sw.club_management_system.domain.Club;
import com.sw.club_management_system.domain.Membership;
import com.sw.club_management_system.domain.User;
import com.sw.club_management_system.service.AuthorizationService;
import com.sw.club_management_system.service.ClubService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    private final AuthorizationService authorizationService;

    // 모든 동아리 조회
    @GetMapping
    public ResponseEntity<List<Club>> getAllClubs() {
        List<Club> clubs = clubService.findAll();
        return ResponseEntity.ok(clubs);
    }

    // 특정 동아리 조회
    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable("id") Integer id) {
        return clubService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 특정 동아리의 모든 멤버 정보 조회. 관리자, 회장 권한
    @GetMapping("/{id}/members")
    public ResponseEntity<List<User>> getMembersByClubId(@PathVariable Integer id, HttpSession session) {
        // 권한 확인
        if (!authorizationService.isAdmin(session) && !authorizationService.isPresidentInClub(id, session)) {
            return ResponseEntity.status(403).body(null);
        }
        // 동아리 멤버 조회
        List<User> members = clubService.getMembersByClubId(id);
        return ResponseEntity.ok(members);
    }

    // 특정 동아리의 멤버십 조회. 관리자, 회장 권한
    @GetMapping("/{id}/memberships")
    public ResponseEntity<List<Membership>> getMembershipByClubId(@PathVariable Integer id, HttpSession session) {
        // 권한 확인
        if (!authorizationService.isAdmin(session) && !authorizationService.isPresidentInClub(id, session)) {
            return ResponseEntity.status(403).body(null);
        }
        // 동아리 멤버십 조회
        List<Membership> memberships = clubService.getMembershipByClubId(id);
        return ResponseEntity.ok(memberships);
    }

    // 새로운 동아리 추가. 관리자 권한
    @PostMapping
    public ResponseEntity<String> createClub(@RequestBody @Valid Club club, HttpSession session) {
        // 권한 확인
        if (!authorizationService.isAdmin(session)) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        clubService.create(club);
        return ResponseEntity.ok("Club created successfully.");
    }

    // 동아리 정보 수정. 관리자, 회장 권한
    @PutMapping("/{id}")
    public ResponseEntity<String> updateClub(@PathVariable Integer id, @RequestBody @Valid Club club, HttpSession session) {
        // 권한 확인
        if (!authorizationService.isAdmin(session) && !authorizationService.isPresidentInClub(id, session)) {
            return ResponseEntity.status(403).body(null);
        }
        boolean isUpdated = clubService.update(id, club);
        if (!isUpdated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Club updated successfully.");
    }

    // 동아리 삭제. 관리자 권한
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClub(@PathVariable Integer id, HttpSession session) {
        // 권한 확인
        if (!authorizationService.isAdmin(session)) {
            return ResponseEntity.status(403).body(null);
        }
        boolean isDeleted = clubService.delete(id);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Club deleted successfully.");
    }
}
