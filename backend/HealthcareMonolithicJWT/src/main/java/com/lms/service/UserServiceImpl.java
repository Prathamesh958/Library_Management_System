package com.lms.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.lms.custom_exceptions.ResourceNotFoundException;
import com.lms.dtos.*;
import com.lms.entities.*;
import com.lms.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final StudentRepository studentRepo;
    private final LibrarianRepository librarianRepo;
    private final UserRepository userRepo;
    private final ModelMapper modelMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    // TOKEN SE PROFILE NIKALNE KE LIYE
    public StudentDTO getStudentById(Long userId) {
        Student student = studentRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));
        StudentDTO dto = modelMapper.map(student, StudentDTO.class);
        dto.setName(student.getUser().getName());
        dto.setEmail(student.getUser().getEmail());
        dto.setStatus(student.getUser().getStatus());
        return dto;
    }

    @Override
    public StudentDTO getStudentByRoll(String rollNo) {
        Student student = studentRepo.findByRollNo(rollNo.trim());
        if (student == null) throw new ResourceNotFoundException("Student not found");
        StudentDTO dto = modelMapper.map(student, StudentDTO.class);
        dto.setName(student.getUser().getName());
        dto.setEmail(student.getUser().getEmail());
        dto.setStatus(student.getUser().getStatus());
        return dto;
    }

    @Override
    public LibrarianDTO getLibrarianByEmpId(String empId) {
        Librarian lib = librarianRepo.findByEmployeeId(empId);
        if (lib == null) throw new ResourceNotFoundException("Librarian not found");
        LibrarianDTO dto = modelMapper.map(lib, LibrarianDTO.class);
        dto.setName(lib.getUser().getName());
        dto.setEmail(lib.getUser().getEmail());
        return dto;
    }

    @Override public UserDTO getUserByEmail(String email) { return null; }

    @Transactional
    public String registerUser(UserRegistrationDTO regDto) {
        User user = new User();
        user.setName(regDto.getName());
        user.setEmail(regDto.getEmail());
        user.setPassword(passwordEncoder.encode(regDto.getPassword()));
        user.setRole(regDto.getRole());
        user.setStatus(Status.ACTIVE);
        User savedUser = userRepo.save(user);

        if (regDto.getRole() == Role.STUDENT) {
            Student student = new Student();
            student.setUser(savedUser);
            student.setRollNo(regDto.getRollNo());
            student.setCourse(regDto.getCourse());
            student.setYear(regDto.getYear());
            studentRepo.save(student);
        } else if (regDto.getRole() == Role.LIBRARIAN) {
            Librarian lib = new Librarian();
            lib.setUser(savedUser);
            lib.setEmployeeId(regDto.getEmployeeId());
            librarianRepo.save(lib);
        }
        return "Registration successful with Role: " + regDto.getRole();
    }
}