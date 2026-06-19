package com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Profile;
import com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.entities.ProfilePersistenceEntity;

public final class ProfilePersistenceAssembler {

    private ProfilePersistenceAssembler() {}

    public static Profile toDomainFromPersistence(ProfilePersistenceEntity entity) {
        if (entity == null) return null;
        var profile = new Profile();
        profile.setId(entity.getId());
        profile.setUserId(entity.getUserId());
        profile.setName(entity.getName());
        profile.setLastName(entity.getLastName());
        profile.setPhoneNumber(entity.getPhoneNumber());
        profile.setAvatarUrl(entity.getAvatarUrl());
        profile.setAvatarPublicId(entity.getAvatarPublicId());
        profile.setGender(entity.getGender());
        profile.setBirthDate(entity.getBirthDate());
        return profile;
    }

    public static ProfilePersistenceEntity toPersistenceFromDomain(Profile profile) {
        if (profile == null) return null;
        var entity = new ProfilePersistenceEntity();
        if (profile.getId() != null) entity.setId(profile.getId());
        entity.setUserId(profile.getUserId());
        entity.setName(profile.getName());
        entity.setLastName(profile.getLastName());
        entity.setPhoneNumber(profile.getPhoneNumber());
        entity.setAvatarUrl(profile.getAvatarUrl());
        entity.setAvatarPublicId(profile.getAvatarPublicId());
        entity.setGender(profile.getGender());
        entity.setBirthDate(profile.getBirthDate());
        return entity;
    }
}
