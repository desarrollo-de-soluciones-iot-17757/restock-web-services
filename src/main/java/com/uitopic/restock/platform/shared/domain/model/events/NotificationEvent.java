package com.uitopic.restock.platform.shared.domain.model.events;

/**
 * Marker interface for notification events in the system. This interface is used to identify events that are related to notifications, such as when inventory falls below minimum stock levels or when a custom supply is deleted. Implementing this interface allows for consistent handling of notification events across the application, enabling the system to trigger appropriate actions (e.g., sending email or push notifications) when these events occur.
 */
public interface NotificationEvent {
}
