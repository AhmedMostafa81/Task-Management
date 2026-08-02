package com.internship.backend.controller;

import com.internship.backend.dto.AuthResponseDTO;
import com.internship.backend.dto.LoginRequestDTO;
import com.internship.backend.dto.RegisterRequestDTO;
import com.internship.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        log.info("Processing registration request for username: {}", request.getUsername());
        long startTime = System.currentTimeMillis();
        try {
            AuthResponseDTO response = authService.register(request);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Registration successful for username: {} - Duration: {}ms", request.getUsername(), duration);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            long duration = System.currentTimeMillis() - startTime;
            log.warn("Registration failed for username: {} - Error: {} - Duration: {}ms",
                    request.getUsername(), e.getMessage(), duration);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Unexpected error during registration for username: {} - Duration: {}ms",
                    request.getUsername(), duration, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Processing login request for username: {}", request.getUsername());
        long startTime = System.currentTimeMillis();
        try {
            AuthResponseDTO response = authService.login(request);
            long duration = System.currentTimeMillis() - startTime;
            log.info("Login successful for username: {} - Duration: {}ms", request.getUsername(), duration);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            long duration = System.currentTimeMillis() - startTime;
            log.warn("Login failed for username: {} - Error: {} - Duration: {}ms",
                    request.getUsername(), e.getMessage(), duration);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Unexpected error during login for username: {} - Duration: {}ms",
                    request.getUsername(), duration, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}