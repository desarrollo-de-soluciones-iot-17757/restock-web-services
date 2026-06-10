package com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.entities.NotificationPersistenceEntity;

/**
 * Assembler class to convert between Notification domain model and NotificationPersistenceEntity.
 * This class provides static methods for the conversion process.
 */
public final class NotificationPersistenceAssembler {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private NotificationPersistenceAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a NotificationPersistenceEntity to a Notification domain model.
     *
     * @param entity the NotificationPersistenceEntity to convert
     * @return the corresponding Notification domain model
     */
    public static Notification toDomainFromPersistence(NotificationPersistenceEntity entity) {
        if (entity == null) return null;

        var notification = new Notification();
        notification.setId(entity.getId());
        notification.setRecipientId(entity.getRecipientId());
        notification.setSourceId(entity.getSourceId());
        notification.setMessage(entity.getMessage());
        notification.setTitle(entity.getTitle());
        notification.setSeverity(entity.getSeverity());
        notification.setStatus(entity.getStatus());

        return notification;
    }

    /**
     * Converts a Notification domain model to a NotificationPersistenceEntity for persistence.
     *
     * @param notification the Notification domain model to convert
     * @return the corresponding NotificationPersistenceEntity for persistence
     */
    public static NotificationPersistenceEntity toPersistenceFromDomain(Notification notification) {

        if (notification == null) return null;

        var entity = new NotificationPersistenceEntity();

        if (notification.getId() != null) {
            entity.setId(notification.getId());
        }

        entity.setRecipientId(notification.getRecipientId());
        entity.setSourceId(notification.getSourceId());
        entity.setMessage(notification.getMessage());
        entity.setTitle(notification.getTitle());
        entity.setSeverity(notification.getSeverity());
        entity.setStatus(notification.getStatus());

        return entity;
    }
}
