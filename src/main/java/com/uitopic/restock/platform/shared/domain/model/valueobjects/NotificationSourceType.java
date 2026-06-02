package com.uitopic.restock.platform.shared.domain.model.valueobjects;

/**
 * Enumeration representing the source type of notification, which can be either "IOT" for notifications originating from IoT devices or "CLOUD" for notifications originating from cloud servers. This enum provides a standardized way to categorize the source of notifications, allowing for better organization and handling of notifications based on their origin.
 */
public enum NotificationSourceType {
    IOT_SERVER("iot"),
    CLOUD_SERVER("cloud");

    private final String value;

    NotificationSourceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value.toUpperCase();
    }
}
