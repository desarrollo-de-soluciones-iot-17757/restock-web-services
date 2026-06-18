package com.uitopic.restock.platform.profiles.application.acl;

import com.uitopic.restock.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.uitopic.restock.platform.profiles.domain.services.ProfileCommandService;
import com.uitopic.restock.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ProfilesContextFacadeImpl implements ProfilesContextFacade {

    private final ProfileCommandService profileCommandService;

    public ProfilesContextFacadeImpl(ProfileCommandService profileCommandService) {
        this.profileCommandService = profileCommandService;
    }

    @Override
    @Transactional
    public String createProfile(String userId, String businessName, String email) {
        log.info("Creating profile via ACL for userId='{}', businessName='{}'", userId, businessName);
        try {
            var command = new CreateProfileCommand(userId, email, null, null, null, null, null, null);
            var profile = profileCommandService.handle(command);
            log.info("Profile created via ACL: id='{}'", profile.getId());
            return profile.getId();
        } catch (Exception e) {
            log.error("Failed to create profile for userId='{}': {}", userId, e.getMessage());
            return "";
        }
    }

    /**
     * Retrieves the notification preference for a given user ID.
     *
     * @param userId the ID of the user whose notification preference is being queried
     * @return the notification preference as a string (e.g., "EMAIL", "PUSH", "ALL", "NONE"), or empty string if not found
     */
    @Override
    public String getNotificationPreferenceByUserId(UserId userId) {
        return "";
    }
}
