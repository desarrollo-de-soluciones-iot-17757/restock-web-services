package com.uitopic.restock.platform.shared.domain.model.events;

/**
 * Marker interface for notification events in the system. This interface is used to identify events that are related to notifications, such as when inventory falls below minimum stock levels or when a custom supply is deleted. Implementing this interface allows for consistent handling of notification events across the application, enabling the system to trigger appropriate actions (e.g., sending email or push notifications) when these events occur.
 */
public interface NotificationEvent {

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
