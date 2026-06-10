package com.uitopic.restock.platform.communications.application.internal.outboundservices.pushprovider;

/**
 * This interface defines the contract for sending push notifications to users. It abstracts the underlying push notification provider (e.g., Firebase Cloud Messaging, Apple Push Notification Service) and allows the application to send notifications without being tightly coupled to a specific implementation.
 * The method `sendPushNotification` takes the recipient's user ID, device token, severity level, source ID, title, and message as parameters. The severity level can be used to categorize notifications (e.g., INFO, WARNING, ERROR) and may influence how the notification is displayed to the user.
 * Implementations of this interface will handle the actual communication with the push notification service and ensure that notifications are delivered to the intended recipients.
 */
public interface PushNotificationService {

    /** Sends a push notification to a user.
     *
     * @param recipientUserId The ID of the user who will receive the notification.
     * @param deviceToken The device token associated with the user's device for push notifications.
     * @param severity The severity level of the notification (e.g., INFO, WARNING, ERROR).
     * @param sourceId The identifier of the source that generated the notification.
     * @param title The title of the notification.
     * @param message The content of the notification message.
     */
    void sendPushNotification(String recipientUserId, String deviceToken, String severity, String sourceId, String title, String message);
}
