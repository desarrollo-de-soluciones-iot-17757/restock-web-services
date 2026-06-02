package com.uitopic.restock.platform.shared.domain.model.valueobjects;

/**
 * Enumeration representing the types of notifications that can be sent to users. This enum defines the different notification types, such as email, push, or all (for sending both email and push notifications). It provides a method to retrieve the notification type in uppercase format, which can be useful for consistent formatting when sending notifications or storing notification preferences.
 */
public enum NotificationType {
    EMAIL("email"),
    PUSH("push"),
    ALL("all");

    private final String type;

    NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type.toUpperCase();
    }
}
