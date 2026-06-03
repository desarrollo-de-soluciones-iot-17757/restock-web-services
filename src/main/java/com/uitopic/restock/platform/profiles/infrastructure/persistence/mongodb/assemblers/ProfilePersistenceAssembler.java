package com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Profile;
import com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.entities.ProfilePersistenceEntity;

public final class ProfilePersistenceAssembler {

    private ProfilePersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static Profile toDomainFromPersistence(ProfilePersistenceEntity entity) {
        return null;
    }

    public static ProfilePersistenceEntity toPersistenceFromDomain(Profile profile) {
        return null;
    }
}
