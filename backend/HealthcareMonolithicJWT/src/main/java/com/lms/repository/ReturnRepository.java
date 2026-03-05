package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entities.ReturnedBook;

public interface ReturnRepository extends JpaRepository<ReturnedBook,Long> {
	ReturnedBook findByIssuedBook_IssueId(Long issueId);

}
