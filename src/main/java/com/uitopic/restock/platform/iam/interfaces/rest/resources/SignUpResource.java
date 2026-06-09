package com.uitopic.restock.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Resource representing the sign-up request.
 *
 * <p>Carries both the authentication credentials and the profile data needed
 * to create the user's profile via the Profiles bounded context ACL.
 */
@Schema(description = "Request resource for user registration")
public record SignUpResource(
        @NotBlank @Schema(description = "Business or company name") String businessName,
        @Email @NotBlank @Schema(description = "User email address") String email,
        @NotBlank @Size(min = 6) @Schema(description = "Password (min 6 characters)") String password,
        @NotBlank @Schema(description = "Role assigned to the user (e.g. OWNER, WORKER)") String role
) {
}
