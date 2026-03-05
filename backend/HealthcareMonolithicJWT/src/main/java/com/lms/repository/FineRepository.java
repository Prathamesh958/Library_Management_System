package com.lms.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.entities.Fine;

public interface FineRepository extends JpaRepository<Fine, Long> {
    // FIX: Student ki User ID se fines nikalne ke liye
    List<Fine> findByStudent_User_UserId(Long userId);
    List<Fine> findByPaidFalse();
    boolean existsByIssuedBook_IssueId(Long issueId);
}