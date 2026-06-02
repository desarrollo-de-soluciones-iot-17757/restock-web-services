package com.uitopic.restock.platform.iam.interfaces.rest.resources;

/**
 * Resource representing an authenticated user with authentication tokens.
 *
 * @param id    the unique identifier of the user
 * @param email the email address of the user
 * @param role  the role assigned to the user
 * @param token the authentication token for the user
 * @param accountId the identifier of the account associated with the user 
 */
public record AuthenticatedUserResource(
                String id,
                String email,
                String role,
                String token,
                String accountId) {
}
