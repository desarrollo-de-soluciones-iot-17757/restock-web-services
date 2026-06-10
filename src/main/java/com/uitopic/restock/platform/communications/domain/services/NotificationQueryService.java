package com.uitopic.restock.platform.communications.domain.services;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.domain.model.queries.GetNotificationByIdQuery;
import com.uitopic.restock.platform.communications.domain.model.queries.GetNotificationsByRecipientUserIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for handling queries related to notifications.
 * This service provides methods to retrieve notifications based on various criteria.
 * It is designed to be implemented by classes that will handle the actual query logic,
 * such as fetching data from a database or an external service.
 */
public interface NotificationQueryService {

    /**
     * Handles the GetNotificationByIdQuery to retrieve a notification by its unique identifier.
     *
     * @param query the query object containing the ID of the notification to retrieve
     * @return an Optional containing the Notification if found, or an empty Optional if not found
     */
    Optional<Notification> handle(GetNotificationByIdQuery query);

    /**
     * Handles the GetNotificationsByRecipientUserIdQuery to retrieve all notifications for a specific recipient user ID.
     *
     * @param query the query object containing the recipient user ID for which to retrieve notifications
     * @return a List of Notifications associated with the specified recipient user ID
     */
    List<Notification> handle(GetNotificationsByRecipientUserIdQuery query);
}
