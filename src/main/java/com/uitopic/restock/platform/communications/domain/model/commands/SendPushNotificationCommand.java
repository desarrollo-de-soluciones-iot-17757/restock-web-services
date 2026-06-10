package com.uitopic.restock.platform.communications.domain.model.commands;

/**
 * Command representing the action of sending a push notification to a user.
 * This command contains all necessary information to create and send a push notification, including the recipient's user ID, source of the notification, title, message content, and severity level.
 */
public record SendPushNotificationCommand(
        String recipientUserId,
        String sourceId,
        String title,
        String message,
        String severity
) {
}
