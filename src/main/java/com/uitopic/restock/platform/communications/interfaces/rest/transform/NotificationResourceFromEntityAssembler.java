package com.uitopic.restock.platform.communications.interfaces.rest.transform;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.NotificationResource;

public class NotificationResourceFromEntityAssembler {

    public static NotificationResource toResourceFromEntity(Notification entity) {
        return new NotificationResource(
                entity.getId(),
                entity.getRecipientId(),
                entity.getSourceId(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getSeverity().name(),
                entity.getStatus().name()
        );
    }
}
