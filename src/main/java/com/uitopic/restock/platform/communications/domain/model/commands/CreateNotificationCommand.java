package com.uitopic.restock.platform.communications.domain.model.commands;

import com.uitopic.restock.platform.communications.domain.model.valueobjects.SourceType;

public record CreateNotificationCommand(
        String recipientUserId,
        String sourceId,
        SourceType sourceType,
        String title,
        String message,
        String severity
) {
}
