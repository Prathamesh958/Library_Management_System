package com.lms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
	private String jwt;
    private String role;
    private String email;
    private Long userId;
}
