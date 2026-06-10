package com.uitopic.restock.platform.communications.domain.model.commands;

public record CreateNotificationCommand(
        String recipientUserId,
        String sourceId,
        String title,
        String message,
        String severity,
        String status
) {
}
