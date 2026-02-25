package com.argenischacon.inventory_sales_system.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils(
                "test-secret-12345678901234567890123456789012",
                3600000L);

        userDetails = new User("testuser", "password", Collections.emptyList());
    }

    @Test
    void generateToken_NotNull() {
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ReturnsCorrectUsername() {
        String token = jwtUtils.generateToken(userDetails);
        String extractedUsername = jwtUtils.extractUsername(token);
        assertEquals("testuser", extractedUsername);
    }

    @Test
    void isTokenValid_WithValidToken_ReturnsTrue() {
        String token = jwtUtils.generateToken(userDetails);
        assertTrue(jwtUtils.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_WithWrongUser_ReturnsFalse() {
        String token = jwtUtils.generateToken(userDetails);
        UserDetails otherUser = new User("otheruser", "password", Collections.emptyList());
        assertFalse(jwtUtils.isTokenValid(token, otherUser));
    }

    @Test
    void isTokenValid_WithExpiredToken_ReturnsFalse() {
        JwtUtils expiredJwtUtils = new JwtUtils(
                "test-secret-12345678901234567890123456789012",
                -1000L);
        String expiredToken = expiredJwtUtils.generateToken(userDetails);
        assertFalse(jwtUtils.isTokenValid(expiredToken, userDetails));
    }

    @Test
    void isTokenValid_WithInvalidToken_ReturnsFalse() {
        assertFalse(jwtUtils.isTokenValid("invalid.token.here", userDetails));
    }
}
