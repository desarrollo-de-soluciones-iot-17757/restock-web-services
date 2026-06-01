package com.uitopic.restock.platform.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for setting up OpenAPI documentation for the Restock Platform application.
 * This class defines the OpenAPI bean that configures the API documentation, including metadata,
 * security schemes, and server information.
 */
@Configuration
public class OpenApiConfiguration {

    // Inject application name from properties file
    @Value("${spring.application.name}")
    String applicationName;

    // Inject application description from properties file
    @Value("${documentation.application.description}")
    String applicationDescription;

    // Inject application version from properties file
    @Value("${documentation.application.version}")
    String applicationVersion;

    // Define OpenAPI bean to configure API documentation
    @Bean
    public OpenAPI restockPlatformOpenApi() {

        // Create OpenAPI instance
        var openApi = new OpenAPI();

        // Configure API metadata
        openApi.info(new Info()
                        .title(this.applicationName)
                        .description(this.applicationDescription)
                        .version(this.applicationVersion)
                        .contact(new Contact().name("Restock Platform Support"))
                        .license(new License().name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Restock Platform wiki Documentation")
                        .url("https://restock.platform.github.io/docs"));

        // Configure security scheme for JWT authentication

        final String securitySchemeName = "bearerAuth";

        openApi.addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));

        // Configure server URL (you can change this to your actual server URL)
        openApi.servers(List.of(
                new Server().url("/")
        ));

        return openApi;
    }
}
