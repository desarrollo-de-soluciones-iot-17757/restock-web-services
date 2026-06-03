package com.uitopic.restock.platform.iam.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

/**
 * Service in the IAM bounded context that acts as an adapter to call the ProfilesContextFacade
 * from the Profiles bounded context. This service is used to create a profile for a newly registered user after a successful sign-up.
 */
@Service
public class ExternalProfilesService {

    // This service acts as an adapter to call the ProfilesContextFacade from the IAM bounded context.
    private final ProfilesContextFacade profilesContextFacade;

    // Constructor injection of the ProfilesContextFacade to allow calling profile-related operations.
    public ExternalProfilesService(ProfilesContextFacade profilesContextFacade) {
        this.profilesContextFacade = profilesContextFacade;
    }

    /**
     * Creates a profile for a newly registered user by calling the ProfilesContextFacade.
     *
     * @param userId the ID of the newly created user
     * @param businessName the business name provided during sign-up
     * @param email the email address of the user
     * @return the ID of the created profile, or empty string if creation failed
     */
    public String createProfileForNewUser(String userId, String businessName, String email) {
        var profileId = profilesContextFacade.createProfile(userId, businessName, email);
        return profileId != null ? profileId : "";
    }
}
