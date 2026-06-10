package com.uitopic.restock.platform.communications.domain.model.queries;

/**
 * This record represents a query to retrieve notifications for a specific recipient user ID.
 * It contains a single field, recipientUserId, which is used to identify the user for whom the notifications are being requested.
 * The record is immutable and provides a concise way to encapsulate the query parameters.
 */
public record GetNotificationsByRecipientUserIdQuery(
        String recipientUserId
) {
}
