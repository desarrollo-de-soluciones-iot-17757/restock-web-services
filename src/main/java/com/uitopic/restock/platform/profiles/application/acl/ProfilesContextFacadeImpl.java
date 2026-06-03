package com.uitopic.restock.platform.profiles.application.acl;

import com.uitopic.restock.platform.profiles.interfaces.acl.ProfilesContextFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the Profiles anti-corruption layer facade.
 * Receives requests from other bounded contexts and delegates to the
 * Profiles application layer.
 *
 * <p>Profile domain is still being built out — this stub logs the intent
 * and returns a placeholder until {@code ProfileCommandService} is complete.
 */
@Slf4j
@Service
public class ProfilesContextFacadeImpl implements ProfilesContextFacade {

    /**
     * Creates a profile for a newly registered user.
     *
     * @param userId       the ID of the newly created user
     * @param businessName the business name provided during sign-up
     * @param email        the email address of the user
     * @param phone        the phone number of the user
     * @param country      the country of the user
     * @return the ID of the created profile, or empty string if creation failed
     */
    @Override
    @Transactional
    public String createProfile(String userId, String businessName, String email, String phone, String country) {
        log.info("Creating profile for user ID: {}, businessName: {}, email: {}, phone: {}, country: {}",
                userId, businessName, email, phone, country);
        // TODO: delegate to ProfileCommandService once domain is complete
        log.warn("ProfileCommandService not yet implemented — profile creation skipped for user ID: {}", userId);
        return "";
    }
}
