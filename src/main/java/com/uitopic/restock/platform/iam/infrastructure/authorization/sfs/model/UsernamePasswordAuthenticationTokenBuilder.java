package com.uitopic.restock.platform.iam.infrastructure.authorization.sfs.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Builder class for creating {@link UsernamePasswordAuthenticationToken}
 * instances.
 *
 * This class provides a convenient way to construct authentication tokens
 * with proper details populated from a {@link UserDetails} object and an
 * {@link HttpServletRequest}.
 */
public class UsernamePasswordAuthenticationTokenBuilder {

    /**
     * Builds a {@link UsernamePasswordAuthenticationToken} from a
     * {@link UserDetails} and an {@link HttpServletRequest}.
     * <p>
     * The token is populated with the user's details and authorities, and the
     * request details are added to the token.
     *
     * @param userDetails the user details containing authentication information
     * @param request     the HTTP request containing request-specific details
     * @return a fully constructed {@link UsernamePasswordAuthenticationToken}
     */
    public static UsernamePasswordAuthenticationToken build(UserDetails userDetails, HttpServletRequest request) {
        var token = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        token.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource()
                .buildDetails(request));
        return token;
    }
}
