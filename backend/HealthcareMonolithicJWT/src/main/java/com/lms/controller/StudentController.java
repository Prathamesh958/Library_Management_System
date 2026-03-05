package com.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dtos.LibrarianDTO;
import com.lms.dtos.StudentDTO;
import com.lms.service.UserService;

@RestController
@RequestMapping("/api/users")

public class StudentController {

    private final UserService userService;

    
    @Autowired
    public StudentController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/student/{rollNo}")
    public ResponseEntity<StudentDTO> getStudentByRoll(@PathVariable String rollNo) {
        return ResponseEntity.ok(userService.getStudentByRoll(rollNo));
    }

    @GetMapping("/librarian/{empId}")
    public ResponseEntity<LibrarianDTO> getLibrarianByEmpId(@PathVariable String empId) {
        return ResponseEntity.ok(userService.getLibrarianByEmpId(empId));
    }
}