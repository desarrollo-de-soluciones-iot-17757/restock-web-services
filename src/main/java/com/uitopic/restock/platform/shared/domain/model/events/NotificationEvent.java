package com.uitopic.restock.platform.shared.domain.model.events;

/**
 * Marker interface for notification events in the system. This interface is used to identify events that are related to notifications, such as when inventory falls below minimum stock levels or when a custom supply is deleted. Implementing this interface allows for consistent handling of notification events across the application, enabling the system to trigger appropriate actions (e.g., sending email or push notifications) when these events occur.
 */
public interface NotificationEvent {

    /**
     * Returns the unique identifier of the source entity that triggered the event. This identifier can be used to correlate the event with the specific entity (e.g., batch, branch, custom supply) that is associated with the notification, allowing for better tracking and management of notifications in the system.
     *
     * @return a unique identifier (e.g., batchId, branchId, customSupplyId) that represents the source of the event
     */
    String getSourceId();

    /**
     * Returns the alert level indicating the severity of the event, which can be used to determine the appropriate actions to take (e.g., restocking, sending notifications). This field is essential for categorizing the notification event and ensuring that the right level of attention is given to the issue based on its severity.
     *
     * @return a string representing the alert level (e.g., "LOW", "MEDIUM", "HIGH") that indicates the severity of the event
     */
    String getAlertLevelName();

    /**
     * Returns the title of the notification to be displayed to the user.
     *
     * @return a short, descriptive title summarizing the notification (e.g., "Low Stock Alert").
     */
    String notificationTitle();

    /**
     * Returns the full message body of the notification.
     *
     * @return a human-readable message with the details of the event
     *         (e.g., "The batch B-001 has 5 units. The minimum stock is 20.").
     */
    String notificationMessage();
}
