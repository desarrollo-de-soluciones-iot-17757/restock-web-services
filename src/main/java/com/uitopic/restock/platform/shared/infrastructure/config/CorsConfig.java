package com.uitopic.restock.platform.shared.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS (Cross-Origin Resource Sharing) configuration for the REST API.
 *
 * This class allows client applications running on different origins to communicate
 * with the API. Specifically, it authorizes requests from http://localhost:4200 (Angular)
 * on all endpoints under /api/**.
 *
 * @author Restock Team
 * @version 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mappings to allow requests from clients on different origins.
     *
     * Configuration details:
     * - Allowed origin: http://localhost:4200 (Angular dev server)
     * - Paths: /api/** (all API endpoints)
     * - Allowed HTTP methods: GET, POST, PUT, DELETE, OPTIONS
     * - Allowed headers: Content-Type, Authorization, and other common headers
     * - Credentials: enabled (cookies, JWT tokens, etc.)
     * - Max cache age: 3600 seconds (1 hour)
     *
     * @param registry the Spring CORS configuration registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/api/**")
            .allowedOrigins("http://localhost:4200")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders(
                "Content-Type",
                "Authorization",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "X-Requested-With",
                "Cache-Control"
            )
            .exposedHeaders(
                "Content-Type",
                "Authorization",
                "Access-Control-Allow-Origin",
                "X-Custom-Header"
            )
            .allowCredentials(true)
            .maxAge(3600);
    }
}

