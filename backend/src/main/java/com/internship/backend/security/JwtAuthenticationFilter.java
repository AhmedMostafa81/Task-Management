package com.internship.backend.security;

import com.internship.backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String requestPath = request.getRequestURI();
            String method = request.getMethod();
            log.debug("Processing request - Method: {}, Path: {}", method, requestPath);
            
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.debug("No valid Authorization header found for request: {} {}", method, requestPath);
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            log.debug("JWT token extracted from Authorization header for request: {} {}", method, requestPath);
            
            username = jwtService.extractUsername(jwt);
            log.debug("Username extracted from JWT: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("Attempting to authenticate user: {} for request: {} {}", username, method, requestPath);
                
                UserDetails userDetails = userRepository.findByUsername(username).orElse(null);

                if (userDetails != null) {
                    log.debug("User details found for username: {}", username);
                    
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        log.debug("JWT token validation successful for user: {}", username);
                        
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        
                        log.info("User authenticated successfully - Username: {}, Request: {} {}", 
                                username, method, requestPath);
                    } else {
                        log.warn("JWT token validation failed for user: {} - Token is expired or invalid", username);
                    }
                } else {
                    log.warn("User details not found in database for username extracted from JWT: {}", username);
                }
            } else if (username == null) {
                log.debug("Could not extract username from JWT token for request: {} {}", method, requestPath);
            } else {
                log.debug("Authentication already present in SecurityContext for request: {} {}", method, requestPath);
            }
            
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error("JWT authentication filter error for request: {} {} - Error: {}", 
                    request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
            filterChain.doFilter(request, response);
        }
    }
}