package com.lms.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FineDTO {
    
    private Long id;
    private Double amount;
    private boolean paid;
    private LocalDateTime fineDate;
    
    // Sirf IDs bhej rahe hain taaki JSON clean rahe
    private Long borrowingId;
    private Long studentId;
    
    // Optional: Agar UI par naam dikhana hai toh ye fields bhi daal sakte ho
    private String studentName;
    private String bookTitle;
}