package com.lms.service;

import java.util.List;
import com.lms.dtos.IssuedBookDTO;
import com.lms.dtos.ReturnedBookDTO;

public interface IssueService {
    IssuedBookDTO issueBook(Long bookId, Long studentId);
    List<IssuedBookDTO> getHistoryByStudent(Long userId); // Changed to userId for Token
    List<IssuedBookDTO> getIssuesByBookId(Long bookId);
    ReturnedBookDTO returnBook(Long issueId);
}