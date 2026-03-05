package com.lms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lms.entities.IssuedBook;

public interface IssueRepository extends JpaRepository<IssuedBook, Long> {
    // FIX: Student -> User -> UserId tak pahunchne ke liye ye naming convention zaruri hai
    List<IssuedBook> findByStudent_User_UserId(Long userId);
    
    List<IssuedBook> findByBook_BookId(Long bookId);
}