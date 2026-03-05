package com.lms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dtos.FineDTO;
import com.lms.service.FineService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    // 1. Fine calculate karne ke liye (Naya Endpoint)
    // 
//    @PostMapping("/calculate/{borrowingId}")
//    public ResponseEntity<String> calculateFine(@PathVariable Long borrowingId) {
//        fineService.calculateAndRecordFine(borrowingId);
//        return ResponseEntity.ok("Fine calculated and updated (if any)!");
//    }

    // 2. to see all unpaid fines
    @GetMapping("/unpaid")
    public ResponseEntity<List<FineDTO>> getUnpaidFines() {
        return ResponseEntity.ok(fineService.getUnpaidFines());
    }

   
    @PutMapping("/pay/{fineId}")
    public ResponseEntity<String> payFine(@PathVariable Long fineId) {
        fineService.payFine(fineId);
        return ResponseEntity.ok("Payment successful! Fine cleared.");
    }

    // 4. to see all fines of specific student
    @GetMapping("/student/{userId}")
    public ResponseEntity<List<FineDTO>> getFinesByStudent(@PathVariable Long userId) {
        return ResponseEntity.ok(fineService.getFinesByStudentId(userId));
    }
}