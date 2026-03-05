package com.lms.security;

import java.io.IOException;
import java.util.List;


import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.dtos.ApiResponse;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomJwtVerificationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                log.info("**** Bearer Token found ");
                String jwt = authHeader.substring(7);

                // 1. Token validate karo
                Claims claims = jwtUtils.validateToken(jwt);

                // 2. Claims se data nikalo
                // Note: Agar user_id token mein String hai toh Long.valueOf lagana padega
                String userIdStr = claims.get("user_id", String.class);
                Long userId = Long.valueOf(userIdStr);
                String role = claims.get("user_role", String.class);
                String email = claims.getSubject();

                // 3. Authorities banao (ROLE_ prefix ke saath)
                List<SimpleGrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                // 4. UserPrincipal object banao (Order: id, email, password, role)
                UserPrincipal principal = new UserPrincipal(userId, email, null, role);

                // 5. Authentication Object (Spring Security wala)
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        principal, null, grantedAuthorities);

                log.info("******* Auth successful for: {}", email);

                // 6. Security Context mein set karo
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            log.error("JWT Verification Failed: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            // Important: Hum yahan error throw nahi karenge, taaki request aage badhe.
            // Agar /login hai toh bina token ke bhi chalna chahiye.
            // Agar protected route hai toh Spring Security aage 401 de dega.
        }

        filterChain.doFilter(request, response);
    }
}