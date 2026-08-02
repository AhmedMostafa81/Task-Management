package com.internship.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        log.debug("Extracting username from JWT token");
        try {
            String username = extractClaim(token, Claims::getSubject);
            log.debug("Username extracted successfully from JWT: {}", username);
            return username;
        } catch (Exception e) {
            log.error("Error extracting username from JWT token: {}", e.getMessage(), e);
            throw e;
        }
    }

    public String generateToken(UserDetails userDetails) {
        log.debug("Generating JWT token for username: {}", userDetails.getUsername());
        try {
            long tokenExpirationTime = System.currentTimeMillis() + jwtExpiration;
            Date expirationDate = new Date(tokenExpirationTime);
            
            String token = Jwts.builder()
                    .claims(new HashMap<>())
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(expirationDate)
                    .signWith(getSignInKey())
                    .compact();
            
            log.info("JWT token generated successfully for username: {} - Expiration: {}", 
                    userDetails.getUsername(), expirationDate);
            log.debug("JWT token length: {} characters", token.length());
            return token;
        } catch (Exception e) {
            log.error("Error generating JWT token for username: {} - Error: {}", 
                    userDetails.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.debug("Validating JWT token for username: {}", userDetails.getUsername());
        try {
            final String username = extractUsername(token);
            boolean isExpired = isTokenExpired(token);
            boolean usernameMatch = username.equals(userDetails.getUsername());
            
            log.debug("Token validation details - Username: {}, Username Match: {}, Is Expired: {}", 
                    username, usernameMatch, isExpired);
            
            boolean isValid = usernameMatch && !isExpired;
            if (isValid) {
                log.debug("JWT token validation successful for username: {}", username);
            } else {
                log.warn("JWT token validation failed for username: {} - Username Match: {}, Is Expired: {}", 
                        username, usernameMatch, isExpired);
            }
            return isValid;
        } catch (Exception e) {
            log.error("Error validating JWT token for username: {} - Error: {}", 
                    userDetails.getUsername(), e.getMessage(), e);
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        log.debug("Checking if JWT token is expired");
        try {
            Date expirationDate = extractExpiration(token);
            Date now = new Date();
            boolean isExpired = expirationDate.before(now);
            
            log.debug("Token expiration check - Expiration Date: {}, Current Date: {}, Is Expired: {}", 
                    expirationDate, now, isExpired);
            return isExpired;
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage(), e);
            throw e;
        }
    }

    private Date extractExpiration(String token) {
        log.debug("Extracting expiration date from JWT token");
        try {
            Date expirationDate = extractClaim(token, Claims::getExpiration);
            log.debug("Expiration date extracted: {}", expirationDate);
            return expirationDate;
        } catch (Exception e) {
            log.error("Error extracting expiration date from JWT token: {}", e.getMessage(), e);
            throw e;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        log.debug("Extracting claim from JWT token");
        try {
            final Claims claims = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            log.debug("Claims extracted successfully from JWT token");
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            log.error("Error extracting claims from JWT token: {}", e.getMessage(), e);
            throw e;
        }
    }

    private SecretKey getSignInKey() {
        log.debug("Retrieving signing key for JWT");
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);
            log.debug("Signing key retrieved successfully");
            return key;
        } catch (Exception e) {
            log.error("Error retrieving signing key: {}", e.getMessage(), e);
            throw e;
        }
    }
}