package com.company.lms.service;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Secret key

    private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey12";

    // Generate signing key

    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Generate token

    public String generateToken(String email) {

        return Jwts.builder()

                .setSubject(email)

                .setIssuedAt(new Date())

                .setExpiration( new Date(System.currentTimeMillis()+ 1000 * 60 * 60))

                .signWith(getSigningKey(),SignatureAlgorithm.HS256).compact();
    }
    
 // Extract email from token

    public String extractEmail(String token) {

        Claims claims = Jwts.parserBuilder()

                        .setSigningKey(
                                getSigningKey())

                        .build()

                        .parseClaimsJws(token)

                        .getBody();

        return claims.getSubject();
        
    }
    
    public boolean isTokenValid(String token,String email) {

        String extractedEmail = extractEmail(token);

        return extractedEmail.equals(email);
        
    }
    
    
    
}