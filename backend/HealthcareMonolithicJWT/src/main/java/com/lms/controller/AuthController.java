package com.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.lms.dtos.AuthResponse;
import com.lms.dtos.LoginRequest;
import com.lms.security.JwtUtils;
import com.lms.security.UserPrincipal;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. Authenticate user 
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String jwt = jwtUtils.generateToken(principal);

        return ResponseEntity
                .ok(new AuthResponse(jwt, principal.getRole(), principal.getEmail(), principal.getUserId()));
    }
}