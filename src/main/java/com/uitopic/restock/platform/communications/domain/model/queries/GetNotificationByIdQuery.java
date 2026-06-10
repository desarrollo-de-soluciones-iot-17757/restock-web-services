package com.uitopic.restock.platform.communications.domain.model.queries;

/**
 * Query to get a notification by its ID.
 *
 * @param notificationId the ID of the notification to retrieve
 */
public record GetNotificationByIdQuery(
        String notificationId
) {

    public GetNotificationByIdQuery {
        if (notificationId == null || notificationId.isBlank()) {
            throw new IllegalArgumentException("Notification ID cannot be null or blank");
        }
    }
}
