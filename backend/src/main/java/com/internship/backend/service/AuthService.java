package com.internship.backend.service;

import com.internship.backend.dto.AuthResponseDTO;
import com.internship.backend.dto.LoginRequestDTO;
import com.internship.backend.dto.RegisterRequestDTO;
import com.internship.backend.entity.User;
import com.internship.backend.repository.UserRepository;
import com.internship.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO request) {
        // 1. Check if username is taken
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        // 2. Create the user and encrypt the password
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // 3. Save to database
        userRepository.save(user);

        // 4. Generate JWT token
        String jwtToken = jwtService.generateToken(user);

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        // 1. Authenticate the user (this automatically checks the password against the hashed DB password)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. If we reach this line, authentication was successful. Fetch the user.
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 3. Generate a new JWT token
        String jwtToken = jwtService.generateToken(user);

        return AuthResponseDTO.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }
}