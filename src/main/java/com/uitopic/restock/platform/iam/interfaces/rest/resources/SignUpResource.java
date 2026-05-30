package com.uitopic.restock.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Resource representing the sign-up request.
 * <p>
 * This record is used to transfer sign-up credentials (business name, email,
 * password, role) between the client and the server.
 * It uses {@link jakarta.validation.constraints} annotations to enforce data
 * validation rules.
 */
public record SignUpResource(
                @NotBlank String businessName,
                @Email @NotBlank String email,
                @NotBlank @Size(min = 6) String password,
                @NotBlank String role) {
}
