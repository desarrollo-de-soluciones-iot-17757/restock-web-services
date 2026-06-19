package com.uitopic.restock.platform.profiles.interfaces.rest.transform;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Profile;
import com.uitopic.restock.platform.profiles.interfaces.rest.resources.ProfileResource;

public final class ProfileResourceFromEntityAssembler {

    private ProfileResourceFromEntityAssembler() {}

    public static ProfileResource toResourceFromEntity(Profile profile) {
        return new ProfileResource(
                profile.getId(),
                profile.getUserId(),
                profile.getName(),
                profile.getLastName(),
                profile.getPhoneNumber(),
                profile.getAvatarUrl(),
                profile.getAvatarPublicId(),
                profile.getGender(),
                profile.getBirthDate()
        );
    }
}
