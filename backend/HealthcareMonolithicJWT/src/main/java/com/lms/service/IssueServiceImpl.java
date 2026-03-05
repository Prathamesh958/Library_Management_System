package com.lms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lms.custom_exceptions.*;
import com.lms.dtos.IssuedBookDTO;
import com.lms.entities.*;
import com.lms.repository.*;

@Service
public class IssueServiceImpl implements IssueService {
    @Autowired private IssueRepository issueRepo;
    @Autowired private BookRepository bookRepo;
    @Autowired private StudentRepository studentRepo;
    @Autowired private ModelMapper modelMapper;

    @Override
    public IssuedBookDTO issueBook(Long bookId, Long studentId) {
        Student student = studentRepo.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        
        if(student.getUser().getStatus() == Status.BLOCKED) {
            throw new APIException("Student is blocked! Clear the fines.");
        }

        Book book = bookRepo.findById(bookId)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        
        if(book.getQuantity() <= 0) {
            throw new BookNotAvailableException("Book is out of stock.");
        }

        IssuedBook issue = new IssuedBook();
        issue.setBook(book);
        issue.setStudent(student);
        issue.setIssueDate(LocalDate.now());
        issue.setDueDate(LocalDate.now().plusDays(7));
        issue.setStatus(Status.ACTIVE); 

        book.setQuantity(book.getQuantity() - 1);
        bookRepo.save(book);

        IssuedBook savedIssue = issueRepo.save(issue);
        
        IssuedBookDTO dto = modelMapper.map(savedIssue, IssuedBookDTO.class);
        dto.setStudentName(student.getUser().getName());
        dto.setBookTitle(book.getTitle());
        dto.setStudentId(student.getUserId());
        dto.setBookId(book.getBookId());
        
        return dto;
    }

    @Override
    public List<IssuedBookDTO> getHistoryByStudent(Long userId) {
        // ASLI FIX: findByStudent_User_UserId call kiya hai
        return issueRepo.findByStudent_User_UserId(userId).stream()
                .map(issue -> {
                    IssuedBookDTO dto = modelMapper.map(issue, IssuedBookDTO.class);
                    dto.setBookId(issue.getBook().getBookId());
                    dto.setStudentId(issue.getStudent().getUserId());
                    dto.setStudentName(issue.getStudent().getUser().getName());
                    dto.setBookTitle(issue.getBook().getTitle());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<IssuedBookDTO> getIssuesByBookId(Long bookId) {
        return issueRepo.findByBook_BookId(bookId).stream()
                .map(issue -> {
                    IssuedBookDTO dto = modelMapper.map(issue, IssuedBookDTO.class);
                    dto.setStudentName(issue.getStudent().getUser().getName());
                    dto.setBookTitle(issue.getBook().getTitle());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public com.lms.dtos.ReturnedBookDTO returnBook(Long issueId) { return null; }
}