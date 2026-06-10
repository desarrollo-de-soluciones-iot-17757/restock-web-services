package com.uitopic.restock.platform.communications.domain.model.commands;

/**
 * Command for marking a notification as read.
 *
 * This command is used to update the status of a notification to indicate that it has been read by the recipient.
 */
public record MarkNotificationAsReadCommand(
        String notificationId
) {
}
