package com.uitopic.restock.platform.communications.interfaces.rest.transform;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.NotificationWrapperResource;

import java.util.List;

/**
 * Assembler class responsible for converting a list of Notification entities into a NotificationWrapperResource.
 * This wrapper resource contains a list of NotificationResource objects and the total count of notifications.
 */
public class NotificationWrapperFromEntitiesAssembler {

    /**
     * Converts a list of Notification entities into a NotificationWrapperResource.
     *
     * @param entities The list of Notification entities to convert.
     * @return A NotificationWrapperResource containing the list of NotificationResource objects and the total count.
     */
    public static NotificationWrapperResource toResourceFromEntities(List<Notification> entities) {
        var notifications = entities.stream()
                .map(NotificationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return new NotificationWrapperResource(notifications, notifications.size());
    }
}
