package com.uitopic.restock.platform.analytics.domain.model.events;

/**
 * Domain event published when an alert is created.
 * Carries alert details to be consumed by the analytics context for metric tracking.
 */
public record AlertCreatedEvent(
        String alertId,
        String accountId,
        String alertType,
        String message
) {
    /**
     * Compact constructor that validates all required fields.
     */
    public AlertCreatedEvent {
        if (alertId == null || alertId.isBlank())
            throw new IllegalArgumentException("alertId cannot be null or blank");
        if (accountId == null || accountId.isBlank())
            throw new IllegalArgumentException("accountId cannot be null or blank");
        if (alertType == null || alertType.isBlank())
            throw new IllegalArgumentException("alertType cannot be null or blank");
        if (message == null || message.isBlank())
            throw new IllegalArgumentException("message cannot be null or blank");
    }
}
