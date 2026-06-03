package com.uitopic.restock.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource representing a user without authentication tokens.
 *
 * @param id the unique identifier of the user
 * @param email the email address of the user
 * @param role the role assigned to the user
 * @param accountId the account id assigned to the user
 */
@Schema(
        name = "UserResource",
        description = "Resource representing a user without authentication tokens."
)
public record UserResource(
        @Schema(description = "The unique identifier of the user", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
        @Schema(description = "The email address of the user", example = "juan@gmail.com")
        String email,
        @Schema(description = "The role assigned to the user", example = "RETAIL")
        String role,
        @Schema(description = "The account id assigned to the user", example = "123e4567-e89b-12d3-a456-426614174000")
        String accountId
) {
}
