package com.uitopic.restock.platform.iam.interfaces.rest.resources;

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
public record SignInResource(
        @Email @NotBlank String email,
        @NotBlank String password) {
}
