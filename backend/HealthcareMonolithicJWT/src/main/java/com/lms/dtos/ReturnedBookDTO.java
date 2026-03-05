package com.lms.dtos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ReturnedBookDTO {
    private Long returnId;
    private Long issueId;
    private String bookTitle;
    private String studentName; 
    private LocalDate returnDate;
    private double fineAmount;
}