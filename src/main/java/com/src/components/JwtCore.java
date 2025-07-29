package com.src.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtCore {
    @Value("${backendbankclientprogram.app.secret}")
    private String secret;

    @Value("${backendbankclientprogram.app.expirationMs}")
    private int lifetime;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(lifetime);

        return Jwts.builder()
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .subject(userDetails.getUsername())
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String getNameFromJwt(String token) {
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        return jws.getPayload().getSubject();
    }
}