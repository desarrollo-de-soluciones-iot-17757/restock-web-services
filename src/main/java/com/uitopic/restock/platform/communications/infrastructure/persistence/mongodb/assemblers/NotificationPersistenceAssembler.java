package com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.entities.NotificationPersistenceEntity;

public final class NotificationPersistenceAssembler {

    private NotificationPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static Notification toDomainFromPersistence(NotificationPersistenceEntity entity) {
        return null;
    }

    public static NotificationPersistenceEntity toPersistenceFromDomain(Notification notification) {
        return null;
    }
}
