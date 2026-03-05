package com.lms.service;

import com.lms.dtos.LibrarianDTO;
import com.lms.dtos.StudentDTO;
import com.lms.dtos.UserDTO;
import com.lms.dtos.UserRegistrationDTO;

public interface UserService {
	// Student logic
	StudentDTO getStudentByRoll(String rollNo);

	// Librarian logic
	LibrarianDTO getLibrarianByEmpId(String empId);

	// Generic User logic (Registration ke liye baad mein kaam aayega)
	UserDTO getUserByEmail(String email);
	
	String registerUser(UserRegistrationDTO regDto);

	StudentDTO getStudentById(Long userId);
}
