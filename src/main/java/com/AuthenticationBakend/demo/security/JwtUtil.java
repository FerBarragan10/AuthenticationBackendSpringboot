package com.AuthenticationBakend.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import java.util.Base64;



@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;



    @PostConstruct  // Agregar aquí, después de las variables
    public void init() {
        System.out.println("VENTAS BACKEND - Original Secret: " + secret);
        System.out.println("VENTAS BACKEND - Base64 Encoded Secret: " + getBase64EncodedSecret());
    }
    private String getBase64EncodedSecret() {
        return Base64.getEncoder().encodeToString(secret.getBytes());
    }




    public String createToken(String username) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT") // Esto añade "typ": "JWT" al header
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getBase64EncodedSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Para debugging
    public void printSecretKeyHash() {
        System.out.println("Secret Key Hash: " + getBase64EncodedSecret());
    }
}
