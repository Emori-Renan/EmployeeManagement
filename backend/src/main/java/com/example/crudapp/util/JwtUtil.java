package com.example.crudapp.util;

import java.util.Date;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10h

    private final SecretKey key;

    public JwtUtil() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256); 
    }

    public String generateToken(String usernameOrEmail, String role) {

        return Jwts.builder()
                .setSubject(usernameOrEmail) 
                .claim("role", role) 
                .setIssuedAt(new Date()) 
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) 
                .signWith(key, SignatureAlgorithm.HS256) 
                .compact(); 
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    private Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key) 
                .build()
                .parseClaimsJws(token) 
                .getBody(); 
    }

    public boolean validateToken(String token, String usernameOrEmail) {
        return (usernameOrEmail.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
