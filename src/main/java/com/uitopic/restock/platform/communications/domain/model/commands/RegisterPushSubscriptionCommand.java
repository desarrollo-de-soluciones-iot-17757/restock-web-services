package com.uitopic.restock.platform.communications.domain.model.commands;

/**
 * Command for registering a push subscription for a user.
 * This command is used to create or update a push subscription for a user, allowing them to receive push notifications.
 * It includes the user's ID, the provider token for the push service, the client platform, and the notification provider.
 */
public record RegisterPushSubscriptionCommand(
        String userId,
        String providerToken,
        String clientPlatform,
        String provider
) {
}
