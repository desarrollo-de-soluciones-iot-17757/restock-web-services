package com.uitopic.restock.platform.profiles.application.acl;

import com.uitopic.restock.platform.profiles.domain.model.commands.CreateBusinessCommand;
import com.uitopic.restock.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.uitopic.restock.platform.profiles.domain.services.BusinessCommandService;
import com.uitopic.restock.platform.profiles.domain.services.ProfileCommandService;
import com.uitopic.restock.platform.profiles.interfaces.acl.ProfilesContextFacade;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfilesContextFacadeImpl implements ProfilesContextFacade {

    private final ProfileCommandService profileCommandService;
    private final BusinessCommandService businessCommandService;

    public ProfilesContextFacadeImpl(ProfileCommandService profileCommandService,
                                     BusinessCommandService businessCommandService) {
        this.profileCommandService = profileCommandService;
        this.businessCommandService = businessCommandService;
    }

    @Override
    public String createProfile(String accountId, String userId, String businessName, String email) {
        log.info("Creating profile via ACL for userId='{}', accountId='{}', businessName='{}'", userId, accountId, businessName);
        try {
            var command = new CreateProfileCommand(accountId, userId, email, null, null, null, null, null, null);
            var profile = profileCommandService.handle(command);
            log.info("Profile created via ACL: id='{}'", profile.getId());

            //var businessCommand = new CreateBusinessCommand(accountId, userId, null, null, null, businessName, null);
            //var business = businessCommandService.handle(businessCommand);
            //log.info("Business created via ACL: id='{}'", business.getId());

            return profile.getId();
        } catch (Exception e) {
            log.error("Failed to create profile and business for userId='{}': {}", userId, e.getMessage());
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
