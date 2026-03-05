package com.lms.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.lms.dtos.*;
import com.lms.security.UserPrincipal;
import com.lms.service.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentDashboardController {

    private final UserService userService;
    private final IssueService issueService;
    private final FineService fineService;

    // 1. My Profile
    @GetMapping("/profile")
    public ResponseEntity<StudentDTO> getMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.getStudentById(principal.getUserId()));
    }

    // 2. My Borrowing History
    @GetMapping("/my-history")
    public ResponseEntity<List<IssuedBookDTO>> getMyHistory(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(issueService.getHistoryByStudent(principal.getUserId()));
    }

    // 3. My Fines
    @GetMapping("/my-fines")
    public ResponseEntity<List<FineDTO>> getMyFines(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(fineService.getFinesByStudentId(principal.getUserId()));
    }
}