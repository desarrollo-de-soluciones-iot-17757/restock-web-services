package com.uitopic.restock.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpResource(
        @NotBlank String businessName,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6) String password,
        @NotBlank String role
) {
}
