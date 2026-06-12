package com.uitopic.restock.platform.shared.domain.model.valueobjects;

/**
 * Enumeration representing the types of notifications that can be sent.
 * This enum defines the different types of notifications that can be sent to users, such as email notifications, push notifications, or both. It is used to specify the preferred notification method when creating notifications for users based on their preferences or the nature of the event that triggered the notification.
 */
public enum NotificationType {
    EMAIL,
    PUSH,
    ALL,
    NONE
}
