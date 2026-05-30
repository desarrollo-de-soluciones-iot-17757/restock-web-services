package com.uitopic.restock.platform.iam.infrastructure.authorization.sfs.pipeline;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * AuthenticationEntryPoint implementation that sends an HTTP 401 Unauthorized
 * error when an unauthenticated user tries to access a protected resource.
 *
 * This component is used by Spring Security to handle authentication failures,
 * such as when a user attempts to access an API endpoint without a valid
 * token.
 */
@Component
public class UnauthorizedRequestHandlerEntryPoint implements AuthenticationEntryPoint {

    /**
     * Called by the Spring Security filter chain when an authentication attempt
     * fails
     * or an unauthenticated user accesses a protected resource.
     *
     * @param request       the HTTP request that led to the authentication failure
     * @param response      the HTTP response to send the error through
     * @param authException the exception that caused the authentication failure
     * @throws IOException if an I/O error occurs while sending the error response
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
