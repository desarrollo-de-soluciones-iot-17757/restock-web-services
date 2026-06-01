package com.uitopic.restock.platform.iam.interfaces.rest.resources;

/**
 * Resource representing a user without authentication tokens.
 *
 * @param id the unique identifier of the user
 * @param email the email address of the user
 * @param role the role assigned to the user
 * @param accountId the account id assigned to the user
 */
public record UserResource(
        String id,
        String email,
        String role,
        String accountId
) {
}
