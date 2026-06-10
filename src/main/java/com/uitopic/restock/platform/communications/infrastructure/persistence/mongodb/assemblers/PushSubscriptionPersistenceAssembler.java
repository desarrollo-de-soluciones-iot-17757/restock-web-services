package com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.communications.domain.model.aggregates.PushSubscription;
import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.entities.PushSubscriptionPersistenceEntity;

/**
 * Assembler class to convert between PushSubscription domain model and PushSubscriptionPersistenceEntity.
 * This class provides static methods for the conversion process.
 */
public class PushSubscriptionPersistenceAssembler {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private PushSubscriptionPersistenceAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a PushSubscriptionPersistenceEntity from MongoDB to a PushSubscription domain model.
     *
     * @param entity pushSubscriptionPersistenceEntity to convert
     * @return the corresponding PushSubscription domain model, or null if the input is null
     */
    public static PushSubscription toDomainFromPersistence(PushSubscriptionPersistenceEntity entity) {
        if (entity == null) return null;

        var pushSubscription = new PushSubscription();
        pushSubscription.setId(entity.getId());
        pushSubscription.setUserId(entity.getUserId());
        pushSubscription.setProviderToken(entity.getProviderToken());
        pushSubscription.setProvider(entity.getProvider());
        pushSubscription.setPlatform(entity.getPlatform());
        pushSubscription.setActive(entity.isActive());

        return pushSubscription;
    }

    /**
     *  Converts a PushSubscription domain model to a PushSubscriptionPersistenceEntity for persistence in MongoDB.
     *
     * @param pushSubscription the PushSubscription domain model to convert
     * @return the corresponding PushSubscriptionPersistenceEntity, or null if the input is null
     */
    public static PushSubscriptionPersistenceEntity toPersistenceFromDomain(PushSubscription pushSubscription) {
        if (pushSubscription == null) return null;

        var entity = new PushSubscriptionPersistenceEntity();

        if (pushSubscription.getId() != null) {
            entity.setId(pushSubscription.getId());
        }

        entity.setUserId(pushSubscription.getUserId());
        entity.setProviderToken(pushSubscription.getProviderToken());
        entity.setProvider(pushSubscription.getProvider());
        entity.setPlatform(pushSubscription.getPlatform());
        entity.setActive(pushSubscription.isActive());

        return entity;
    }
}
