package com.uitopic.restock.platform.communications.domain.model.commands;

public record SendPushNotificationCommand(
        String recipientUserId,
        String sourceId,
        String title,
        String message,
        String severity
) {
}
