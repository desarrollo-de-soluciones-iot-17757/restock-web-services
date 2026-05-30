package com.uitopic.restock.platform.iam.infrastructure.authorization.sfs.pipeline;

import com.uitopic.restock.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.uitopic.restock.platform.iam.infrastructure.authorization.sfs.model.UsernamePasswordAuthenticationTokenBuilder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that intercepts incoming HTTP requests to extract and validate a Bearer JWT token
 * from the Authorization header, establishing the Spring Security authentication context if valid.
 */
@Slf4j
@Component
public class BearerAuthorizationRequestFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public BearerAuthorizationRequestFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Intercepts the request and filters it to authenticate bearer token credentials.
     * Extracts token, resolves username, validates token freshness, and updates SecurityContext.
     *
     * @param request the HTTP servlet request to filter
     * @param response the HTTP servlet response
     * @param filterChain the filter chain to execute next
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractBearerToken(request);
            if (token != null) {
                String username = tokenService.extractUsername(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    var userDetails = userDetailsService.loadUserByUsername(username);
                    if (tokenService.validateToken(token, userDetails)) {
                        SecurityContextHolder.getContext().setAuthentication(
                                UsernamePasswordAuthenticationTokenBuilder.build(userDetails, request));
                    }
                }
            }
        } catch (Exception e) {
            log.debug("JWT validation failed: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
