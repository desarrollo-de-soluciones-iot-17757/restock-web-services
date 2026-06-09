package com.uitopic.restock.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource representing an authenticated user with authentication tokens.
 *
 * @param id    the unique identifier of the user
 * @param email the email address of the user
 * @param role  the role assigned to the user
 * @param token the authentication token for the user
 * @param accountId the identifier of the account associated with the user 
 */
@Schema(
        name = "AuthenticatedUserResource",
        description = "Resource representing an authenticated user with authentication tokens."
)
public record AuthenticatedUserResource(
        @Schema(description = "The unique identifier of the user", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
        @Schema(description = "The email address of the user", example = "juan@gmail.com")
        String email,
        @Schema(description = "The role assigned to the user", example = "RETAIL")
        String role,
        @Schema(description = "The authentication token for the user", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
        String token,
        @Schema(description = "The account id associated with the user", example = "123e4567-e89b-12d3-a456-426614174000")
        String accountId
) {
}
