package com.uitopic.restock.platform.communications.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Record representing a notification resource in the REST API.
 * This resource is used to transfer notification data between the server and clients.
 */
@Schema(
        name = "NotificationResource",
        description = "Represents a notification resource in the REST API."
)
public record NotificationResource(
        @Schema(description = "Unique identifier of the notification")
        String id,

        @Schema(description = "Recipient user identifier")
        String recipientUserId,

        @Schema(description = "Source identifier that originated the notification")
        String sourceId,

        @Schema(description = "Notification title")
        String title,

        @Schema(description = "Notification message")
        String message,

        @Schema(description = "Notification severity")
        String severity,

        @Schema(description = "Notification status")
        String status
) {
}
