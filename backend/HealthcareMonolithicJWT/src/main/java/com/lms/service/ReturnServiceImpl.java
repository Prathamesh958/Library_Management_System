package com.lms.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lms.custom_exceptions.*;
import com.lms.dtos.ReturnedBookDTO;
import com.lms.entities.*;
import com.lms.repository.*;

@Service
@Transactional
public class ReturnServiceImpl implements ReturnService {
    @Autowired private ReturnRepository returnRepo;
    @Autowired private IssueRepository issueRepo;
    @Autowired private BookRepository bookRepo;
    @Autowired private FineService fineService;

    @Override
    public ReturnedBookDTO returnBook(Long issueId) {
        IssuedBook issue = issueRepo.findById(issueId)
            .orElseThrow(() -> new ResourceNotFoundException("Issue ID is not present in database!"));

        if (issue.getStatus() == Status.RETURNED) {
            throw new APIException("This book is already returned!");
        }
        
        
        

        fineService.calculateAndRecordFine(issueId);

        issue.setStatus(Status.RETURNED);
        Book book = issue.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepo.save(book);
        issueRepo.save(issue);

        ReturnedBook rb = new ReturnedBook();
        rb.setIssuedBook(issue);
        rb.setReturnDate(LocalDate.now()); // <--- FIX: LocalDate use kiya hai
        
        ReturnedBook savedReturn = returnRepo.save(rb);

        ReturnedBookDTO dto = new ReturnedBookDTO();
        dto.setReturnId(savedReturn.getReturnId()); 
        dto.setIssueId(issue.getIssueId());
        dto.setReturnDate(savedReturn.getReturnDate());
        
        if (issue.getBook() != null) {
            dto.setBookTitle(issue.getBook().getTitle());
        }
        
        if (issue.getStudent() != null && issue.getStudent().getUser() != null) {
            dto.setStudentName(issue.getStudent().getUser().getName());
        }

        return dto;
    }
}