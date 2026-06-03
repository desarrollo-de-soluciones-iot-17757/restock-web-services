package com.uitopic.restock.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Resource representing the sign-in request.
 * <p>
 * This record is used to transfer sign-in credentials (email and password)
 * between the client and the server.
 * It uses {@link jakarta.validation.constraints} annotations to enforce data
 * validation rules.
 */
@Schema(
        name = "SignInResource",
        description = "Resource representing the sign-in request, containing the user's email and password."
)
public record SignInResource(
        @Schema(description = "The user's email address, used for authentication.", example = "juan@gmail.com")
        @Email @NotBlank String email,
        @Schema(description = "The user's password, used for authentication.", example = "password123")
        @NotBlank String password) {
}
