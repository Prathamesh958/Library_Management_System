package com.lms.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lms.custom_exceptions.ResourceNotFoundException;
import com.lms.dtos.FineDTO;
import com.lms.entities.Fine;
import com.lms.entities.IssuedBook;
import com.lms.repository.FineRepository;
import com.lms.repository.IssueRepository;

import jakarta.transaction.Transactional;

@Service
public class FineServiceImpl implements FineService {

    @Autowired private FineRepository fineRepo;
    @Autowired private IssueRepository issueRepo;
    @Autowired private ModelMapper modelMapper;

    @Override
    @Transactional
    public void calculateAndRecordFine(Long issueId) {
        IssuedBook issue = issueRepo.findById(issueId)
            .orElseThrow(() -> new ResourceNotFoundException("Issue record not found!!!"));

        // Logic: Agar fine pehle se exists karta hai toh dubara mat banao
        if (fineRepo.existsByIssuedBook_IssueId(issueId)) return;

        LocalDate dueDate = issue.getDueDate();
        LocalDate returnDate = LocalDate.now(); 

        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            double fineAmount = daysLate * 5.0; 

            Fine fine = new Fine();
            fine.setAmount(fineAmount);
            fine.setPaid(false);
            fine.setStudent(issue.getStudent());
            fine.setIssuedBook(issue);
            fine.setFineDate(returnDate);
            
            fineRepo.save(fine);
        }
    }

    @Override
    public void payFine(Long fineId) {
        Fine fine = fineRepo.findById(fineId)
            .orElseThrow(() -> new ResourceNotFoundException("Fine record not found"));
        fine.setPaid(true);
        fineRepo.save(fine);
    }

    @Override
    public List<FineDTO> getUnpaidFines() {
        return fineRepo.findByPaidFalse().stream()
                .map(this::mapToFineDTO) // Helper method call
                .collect(Collectors.toList());
    }

    @Override
    public List<FineDTO> getFinesByStudentId(Long userId) {
        return fineRepo.findByStudent_User_UserId(userId).stream()
                .map(this::mapToFineDTO) // Helper method call
                .collect(Collectors.toList());
    }

    
    private FineDTO mapToFineDTO(Fine fine) {
        
        FineDTO dto = modelMapper.map(fine, FineDTO.class);
        
        
        dto.setId(fine.getId()); 
        
        
        if (fine.getIssuedBook() != null) {
            dto.setBorrowingId(fine.getIssuedBook().getIssueId()); // BorrowID Fix
            dto.setBookTitle(fine.getIssuedBook().getBook().getTitle()); // Title Fix
        }
       
        if (fine.getStudent() != null) {
            dto.setStudentId(fine.getStudent().getUserId()); // StudentID Fix
            dto.setStudentName(fine.getStudent().getUser().getName()); // Name Fix
        }
        
       
        if (fine.getFineDate() != null) {
            dto.setFineDate(fine.getFineDate().atStartOfDay());
        }
        
        return dto;
    }
}