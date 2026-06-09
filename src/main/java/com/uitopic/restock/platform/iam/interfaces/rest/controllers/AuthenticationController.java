package com.uitopic.restock.platform.iam.interfaces.rest.controllers;

import com.uitopic.restock.platform.iam.domain.model.commands.SignInCommand;
import com.uitopic.restock.platform.iam.domain.model.commands.SignUpCommand;
import com.uitopic.restock.platform.iam.domain.services.UserCommandService;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.SignInResource;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.SignUpResource;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.UserResource;
import com.uitopic.restock.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for authentication operations in the IAM context.
 * Exposes endpoints for user registration (sign-up) and authentication (sign-in).
 *
 * <p>Token generation is handled inside the application layer ({@link UserCommandService}),
 * not here. The controller only maps resources to commands and formats responses.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication endpoints.")
public class AuthenticationController {

    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    /**
     * Authenticates a user with the provided credentials.
     * Token generation is delegated to the application layer.
     *
     * @param resource the sign-in details including email and password
     * @return 200 with the authenticated user resource and token, or 401 if credentials are invalid
     */
    @Operation(summary = "Sign in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserResource> signIn(@Valid @RequestBody SignInResource resource) {
        log.info("POST /sign-in — email: {}", resource.email());
        return userCommandService.handle(new SignInCommand(resource.email(), resource.password()))
                .map(entry -> {
                    log.info("Sign-in successful for user ID: {}", entry.getKey().getId());
                    return ResponseEntity.ok(
                            UserResourceFromEntityAssembler.toResourceFromEntity(entry.getKey(), entry.getValue()));
                })
                .orElseGet(() -> {
                    log.warn("Sign-in failed for email: {}", resource.email());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                });
    }

    /**
     * Registers a new user account and creates the associated profile.
     *
     * @param resource the sign-up details including credentials and profile data
     * @return 201 Created with the registered user resource
     */
    @Operation(summary = "Sign up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered"),
            @ApiResponse(responseCode = "409", description = "Email already registered"),
            @ApiResponse(responseCode = "400", description = "Missing or invalid fields")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<UserResource> signUp(@Valid @RequestBody SignUpResource resource) {
        log.info("POST /sign-up — email: {}, role: {}", resource.email(), resource.role());

        var user = userCommandService.handle(
                new SignUpCommand(
                        resource.businessName(),
                        resource.email(),
                        resource.password(),
                        resource.role()
                )
        );

        log.info("User registered successfully — ID: {}, email: {}", user.getId(), user.getEmail().email());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResourceFromEntityAssembler.toResourceFromEntity(user));
    }
}
