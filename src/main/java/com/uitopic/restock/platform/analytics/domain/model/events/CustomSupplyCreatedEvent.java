package com.uitopic.restock.platform.analytics.domain.model.events;

/**
 * Domain event published when a custom supply is created.
 * Carries supply details to be consumed by the analytics context for metric tracking.
 */
public record CustomSupplyCreatedEvent(
        String customSupplyId,
        String supplyId,
        String accountId,
        String name
) {
    /**
     * Compact constructor that validates all required fields.
     */
    public CustomSupplyCreatedEvent {
        if (customSupplyId == null || customSupplyId.isBlank())
            throw new IllegalArgumentException("customSupplyId cannot be null or blank");
        if (supplyId == null || supplyId.isBlank())
            throw new IllegalArgumentException("supplyId cannot be null or blank");
        if (accountId == null || accountId.isBlank())
            throw new IllegalArgumentException("accountId cannot be null or blank");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name cannot be null or blank");
    }
}
