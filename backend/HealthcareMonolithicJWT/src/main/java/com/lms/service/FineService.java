package com.lms.service;

import java.util.List;

import com.lms.dtos.FineDTO;

public interface FineService {
    void payFine(Long fineId); // Fine pay karne ke liye
    List<FineDTO> getUnpaidFines(); // Librarian ke liye: Sabhi pending fines
    List<FineDTO> getFinesByStudentId(Long userId); // Student ke liye: Uske apne fines
    void calculateAndRecordFine(Long issueId); // Book return ke waqt fine check karne ke liye
}