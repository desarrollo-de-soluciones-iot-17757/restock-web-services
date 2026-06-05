package com.uitopic.restock.platform.iam.infrastructure.authorization.sfs.configuration;

import com.uitopic.restock.platform.iam.infrastructure.authorization.sfs.pipeline.BearerAuthorizationRequestFilter;
import com.uitopic.restock.platform.iam.infrastructure.authorization.sfs.pipeline.UnauthorizedRequestHandlerEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Web security configuration class for the IAM bounded context.
 * Sets up CORS, CSRF disablement, stateless sessions, unauthorized access endpoints, and filter chains.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final BearerAuthorizationRequestFilter bearerFilter;
    private final UnauthorizedRequestHandlerEntryPoint unauthorizedEntryPoint;

    public WebSecurityConfiguration(BearerAuthorizationRequestFilter bearerFilter,
            UnauthorizedRequestHandlerEntryPoint unauthorizedEntryPoint) {
        this.bearerFilter = bearerFilter;
        this.unauthorizedEntryPoint = unauthorizedEntryPoint;
    }

    /**
     * Configures the main HTTP security filter chain.
     * Specifies public endpoints (like auth, swagger, error, and OPTIONS preflights) and enforces
     * authentication on all other endpoints, incorporating the JWT Bearer filter.
     *
     * @param http the HttpSecurity builder to configure
     * @return the built SecurityFilterChain
     * @throws Exception if an error occurs during building the security filter chain
     */
    @Bean   
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(configurer -> configurer.configurationSource(request -> {
                    var cors = new CorsConfiguration();
                    cors.setAllowedOriginPatterns(List.of("*"));
                    cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    cors.setAllowedHeaders(List.of("*"));
                    cors.setExposedHeaders(List.of("Authorization", "Access-Control-Allow-Origin"));
                    cors.setAllowCredentials(true);
                    cors.setMaxAge(3600L);
                    return cors;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedEntryPoint))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(bearerFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}