package com.lms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dtos.IssuedBookDTO;
import com.lms.dtos.ReturnedBookDTO;
import com.lms.service.IssueService;
import com.lms.service.ReturnService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final ReturnService returnService;

    @PostMapping("/issue")
    public ResponseEntity<IssuedBookDTO> issueBook(@RequestParam Long bookId, @RequestParam Long studentId) {
        return ResponseEntity.ok(issueService.issueBook(bookId, studentId));
    }

    @PostMapping("/return/{issueId}")
    public ResponseEntity<ReturnedBookDTO> returnBook(@PathVariable Long issueId) {
        return ResponseEntity.ok(returnService.returnBook(issueId));
    }

    // Repo: findByStudent_UserId
    @GetMapping("/history/student/{userId}")
    public ResponseEntity<List<IssuedBookDTO>> getStudentHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(issueService.getHistoryByStudent(userId));
    }
}