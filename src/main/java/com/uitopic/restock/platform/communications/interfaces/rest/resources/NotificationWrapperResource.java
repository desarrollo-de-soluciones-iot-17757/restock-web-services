package com.uitopic.restock.platform.communications.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Record representing a wrapper resource for notifications in the REST API.
 * This resource encapsulates a list of NotificationResource objects and the total count of notifications.
 */
@Schema(
        name = "NotificationWrapperResource",
        description = "A wrapper resource for notifications, encapsulating the details of a notification."
)
public record NotificationWrapperResource(
        @Schema(description = "Notifications")
        List<NotificationResource> notifications,

        @Schema(description = "Total notification count")
        int total
) {
}
