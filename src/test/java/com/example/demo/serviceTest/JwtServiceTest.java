package com.example.demo.serviceTest;

import com.example.demo.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;
    private String token;
    private final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    @BeforeEach
    void setUp() {
        // Set up the secret key and expiration time using ReflectionTestUtils
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRATION_TIME);

        // Create a test UserDetails
        userDetails = new User(
                "testuser@example.com",
                "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Generate a token for testing
        token = jwtService.generateToken(userDetails);
    }

    @Test
    void extractUsername_Success() {
        String username = jwtService.extractUsername(token);
        assertEquals("testuser@example.com", username);
    }

    @Test
    void generateToken_Success() {
        String generatedToken = jwtService.generateToken(userDetails);
        assertNotNull(generatedToken);
        assertTrue(generatedToken.length() > 0);
    }

    @Test
    void generateTokenWithExtraClaims_Success() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("testKey", "testValue");

        String generatedToken = jwtService.generateToken(extraClaims, userDetails);

        assertNotNull(generatedToken);
        assertTrue(generatedToken.length() > 0);
    }

    @Test
    void isTokenValid_ValidToken_ReturnsTrue() {
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_InvalidUsername_ReturnsFalse() {
        UserDetails differentUser = new User(
                "different@example.com",
                "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        boolean isValid = jwtService.isTokenValid(token, differentUser);
        assertFalse(isValid);
    }

    @Test
    void getExpirationTime_Success() {
        long expirationTime = jwtService.getExpirationTime();
        assertEquals(EXPIRATION_TIME, expirationTime);
    }

    @Test
    void extractClaim_Success() {
        String subject = jwtService.extractClaim(token, Claims::getSubject);
        assertEquals("testuser@example.com", subject);
    }

    @Test
    void tokenContainsRole_Success() {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<?> roles = claims.get("role", List.class);
        assertNotNull(roles);
        assertTrue(roles.contains("ROLE_USER"));
    }

    @Test
    void tokenExpiration_Success() {
        Date expirationDate = jwtService.extractClaim(token, Claims::getExpiration);
        Date now = new Date();
        assertTrue(expirationDate.after(now));
    }

    @Test
    void tokenIssuedAt_Success() {
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);
        Date now = new Date();
        assertNotNull(issuedAt);
        assertTrue(issuedAt.before(now) || issuedAt.equals(now));
    }

}