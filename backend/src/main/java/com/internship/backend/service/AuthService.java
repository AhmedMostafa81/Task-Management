package com.internship.backend.service;

import com.internship.backend.dto.AuthResponseDTO;
import com.internship.backend.dto.LoginRequestDTO;
import com.internship.backend.dto.RegisterRequestDTO;
import com.internship.backend.entity.User;
import com.internship.backend.repository.UserRepository;
import com.internship.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        log.debug("Starting registration process for username: {}", request.getUsername());
        
        // Check if username is taken
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Registration failed - Username already exists: {}", request.getUsername());
            throw new IllegalArgumentException("Username is already taken!");
        }
        log.debug("Username availability check passed for: {}", request.getUsername());

        // Create the user and encrypt the password
        log.debug("Encrypting password for username: {}", request.getUsername());
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // Save to database
        log.debug("Saving user to database: {}", request.getUsername());
        User savedUser = userRepository.save(user);
        log.info("User saved successfully to database: {}", request.getUsername());

        // Generate JWT token
        log.debug("Generating JWT token for username: {}", request.getUsername());
        String jwtToken = jwtService.generateToken(user);
        log.debug("JWT token generated successfully for username: {}", request.getUsername());

        log.info("Registration completed successfully for username: {}", request.getUsername());
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        log.debug("Starting login process for username: {}", request.getUsername());
        
        // Authenticate the user
        log.debug("Authenticating credentials for username: {}", request.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            log.debug("Authentication successful for username: {}", request.getUsername());
        } catch (Exception e) {
            log.warn("Authentication failed for username: {} - Error: {}", request.getUsername(), e.getMessage());
            throw e;
        }

        log.debug("Fetching user details from database for username: {}", request.getUsername());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.error("User not found in database after successful authentication: {}", request.getUsername());
                    return new IllegalArgumentException("User not found");
                });
        log.debug("User details fetched successfully for username: {}", request.getUsername());

        // Generate a new JWT token
        log.debug("Generating JWT token for login - username: {}", request.getUsername());
        String jwtToken = jwtService.generateToken(user);
        log.debug("JWT token generated successfully for login - username: {}", request.getUsername());

        log.info("Login completed successfully for username: {}", request.getUsername());
        return AuthResponseDTO.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }
}