package com.uitopic.restock.platform.iam.interfaces.acl;

public interface IamContextFacade {

    /**
     * Checks whether a user with the given email exists in the system.
     * Used by other bounded contexts to validate user existence without
     * accessing IAM internals directly.
     *
     * @param email the email address to check
     * @return true if a user with that email is registered, false otherwise
     */
    boolean existsUserByEmail(String email);

    /**
     * Retrieves the account ID associated with the given user ID.
     * Returns an empty string if the user does not exist or has no account
     * assigned.
     *
     * @param userId the ID of the user
     * @return the account ID as a string, or empty string if not found
     */
    String fetchAccountIdByUserId(String userId);
}
