package com.uitopic.restock.platform.iam.interfaces.rest.controllers;

import com.uitopic.restock.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.uitopic.restock.platform.iam.domain.model.commands.SignInCommand;
import com.uitopic.restock.platform.iam.domain.model.commands.SignUpCommand;
import com.uitopic.restock.platform.iam.domain.services.UserCommandService;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.SignInResource;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.SignUpResource;
import com.uitopic.restock.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication endpoints.")
public class AuthenticationController {

    private final UserCommandService userCommandService;
    private final TokenService tokenService;

    public AuthenticationController(UserCommandService userCommandService, TokenService tokenService) {
        this.userCommandService = userCommandService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Sign in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserResource> signIn(@Valid @RequestBody SignInResource resource) {
        return userCommandService.handle(new SignInCommand(resource.email(), resource.password()))
                .map(user -> ResponseEntity.ok(
                        UserResourceFromEntityAssembler.toResourceFromEntity(user, tokenService.generateToken(user))))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @Operation(summary = "Sign up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered"),
            @ApiResponse(responseCode = "409", description = "Email already registered"),
            @ApiResponse(responseCode = "400", description = "Missing or invalid fields")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticatedUserResource> signUp(@Valid @RequestBody SignUpResource resource) {
        var user = userCommandService.handle(
                new SignUpCommand(resource.businessName(), resource.email(), resource.password(), resource.role()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResourceFromEntityAssembler.toResourceFromEntity(user, tokenService.generateToken(user)));
    }
}
