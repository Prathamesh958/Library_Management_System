package com.lms.dtos;

import com.lms.entities.Role;
import com.lms.entities.Status;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class UserDTO {
    private Long userId;
    @NotBlank(message = "Name is required")
    private String name;
    @Email(message = "Invalid email format")
    private String email;
    private Role role;
    private Status status;
}