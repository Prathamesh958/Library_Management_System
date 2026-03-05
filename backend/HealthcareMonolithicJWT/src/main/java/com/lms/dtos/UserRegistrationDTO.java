package com.lms.dtos;

import com.lms.entities.Role;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    // User details
    private String name;
    private String email;
    private String password;
    private Role role; // STUDENT or LIBRARIAN

    // Student specific (Only if role is STUDENT)
    private String rollNo;
    private String course;
    private int year;

    // Librarian specific (Only if role is LIBRARIAN)
    private String employeeId;
}