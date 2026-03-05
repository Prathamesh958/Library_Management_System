package com.lms.dtos;

import java.time.LocalDate;

import com.lms.entities.Status;

import lombok.Data;

@Data
public class IssuedBookDTO {
    private Long issueId;
    private Long bookId;
    private String bookTitle;
    private Long studentId;
    private String studentName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private Status status;
}