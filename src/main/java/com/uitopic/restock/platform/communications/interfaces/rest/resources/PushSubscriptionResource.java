package com.uitopic.restock.platform.communications.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Record representing a push notification subscription resource in the REST API.
 * This resource is used to transfer push subscription data between the server and clients.
 */
@Schema(description = "Response resource representing a push notification subscription")
public record PushSubscriptionResource(
        @Schema(description = "Unique identifier of the push subscription")
        String id,

        @Schema(description = "User identifier")
        String userId,

        @Schema(description = "Client platform")
        String clientPlatform,

        @Schema(description = "Notification provider")
        String provider,

        @Schema(description = "Whether this subscription is active")
        boolean active
) {
}
