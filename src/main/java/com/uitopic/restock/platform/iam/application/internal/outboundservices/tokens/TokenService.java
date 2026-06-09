package com.uitopic.restock.platform.iam.application.internal.outboundservices.tokens;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Service interface for generating, validating, and extracting information from
 * JSON Web Tokens (JWT).
 */
public interface TokenService {

    /**
     * Generates a JWT token for the given user.
     *
     * @param user the user for whom to generate the token
     * @return the generated JWT token as a String
     */
    String generateToken(User user);

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token to extract the username from
     * @return the extracted username
     */
    String extractUsername(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
