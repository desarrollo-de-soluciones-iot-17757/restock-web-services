package com.uitopic.restock.platform.profiles.interfaces.acl;

/**
 * Anti-corruption layer facade for the Profiles bounded context.
 * Exposes profile operations to other bounded contexts using primitive types only.
 */
public interface ProfilesContextFacade {

    /**
     * Creates a profile for a newly registered user.
     * Called by the IAM bounded context after a successful sign-up.
     *
     * @param userId       the ID of the newly created user
     * @param businessName the business name provided during sign-up
     * @param email        the email address of the user
     * @param phone        the phone number of the user
     * @param country      the country of the user
     * @return the ID of the created profile, or empty string if creation failed
     */
    String createProfile(String userId, String businessName, String email, String phone, String country);
}
