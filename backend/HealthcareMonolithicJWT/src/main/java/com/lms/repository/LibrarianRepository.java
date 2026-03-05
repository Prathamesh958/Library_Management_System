package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entities.Librarian;

public interface LibrarianRepository extends JpaRepository<Librarian , Long> {
	Librarian findByEmployeeId(String employeeId);
	
}
