package com.uitopic.restock.platform.communications.domain.repositories;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;

import java.util.List;

/**
 * Repository interface for managing Notification entities.
 * This interface defines the contract for saving and retrieving notifications from the data store.
 * Implementations of this interface will handle the actual persistence logic, such as interacting with a database.
 */
public interface NotificationRepository {

    /**
     * Saves a notification to the data store.
     *
     * @param notification The notification entity to be saved.
     * @return The saved notification entity, potentially with an assigned ID or other generated fields.
     */
    Notification save(Notification notification);

    /**
     * Retrieves a list of notifications for a specific recipient user ID.
     *
     * @param recipientUserId The ID of the recipient user for whom to retrieve notifications.
     * @return A list of notifications associated with the specified recipient user ID.
     */
    List<Notification> findByRecipientUserId(String recipientUserId);

    /**
     * Retrieves a notification by its unique identifier.
     *
     * @param notificationId The unique identifier of the notification to retrieve.
     * @return The notification entity associated with the specified ID, or null if not found.
     */
    Notification findById(String notificationId);

    /**
     * Checks if a notification exists in the data store by its unique identifier.
     *
     * @param notificationId The unique identifier of the notification to check for existence.
     * @return true if a notification with the specified ID exists, false otherwise.
     */
    Boolean existsById(String notificationId);
}
