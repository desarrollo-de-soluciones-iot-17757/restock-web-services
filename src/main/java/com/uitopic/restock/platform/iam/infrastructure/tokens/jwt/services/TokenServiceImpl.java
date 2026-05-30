package com.uitopic.restock.platform.iam.infrastructure.tokens.jwt.services;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.infrastructure.tokens.jwt.BearerTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Implementation of {@link BearerTokenService} that generates, validates,
 * and parses JSON Web Tokens (JWT) using hs256 signing.
 */
@Service
public class TokenServiceImpl implements BearerTokenService {

    @Value("${authorization.jwt.secret}")
    private String secret;

    @Value("${authorization.jwt.expiration.days}")
    private int expirationDays;

    /**
     * Generates a cryptographic HMAC secret key from the configured JWT secret.
     *
     * @return the SecretKey used for signing and verifying JWTs
     */
    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token for the specified user containing their email as subject
     * and their role as a claim, signed with the HMAC secret key.
     *
     * @param user the user for whom the token is generated
     * @return the compact, URL-safe JWT string representation
     */
    @Override
    public String generateToken(User user) {
        long expirationMs = (long) expirationDays * 24 * 60 * 60 * 1000;
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().getType().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey())
                .compact();
    }

    /**
     * Extracts the subject (username/email) from the provided JWT token.
     *
     * @param token the JWT token from which to extract the username
     * @return the extracted username string
     */
    @Override
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Validates whether the given JWT token is valid and corresponds to the specified user details.
     * Checks if the username matches and if the token expiration date is in the future.
     *
     * @param token the JWT token to validate
     * @param userDetails the user details to validate against
     * @return true if the token is valid and matches the user, false otherwise
     */
    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = parseClaims(token);
        String subject = claims.getSubject();
        boolean notExpired = claims.getExpiration().after(new Date());
        return subject.equals(userDetails.getUsername()) && notExpired;
    }

    /**
     * Parses the JWT token's claims payload, verifying the signature using the signing key.
     *
     * @param token the JWT token to parse
     * @return the token's claims payload
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
