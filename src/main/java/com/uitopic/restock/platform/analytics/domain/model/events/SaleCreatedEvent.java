package com.uitopic.restock.platform.analytics.domain.model.events;

/**
 * Domain event published when a sale is created.
 * Carries sale details to be consumed by the analytics context for metric tracking.
 */
public record SaleCreatedEvent(
        String saleId,
        String branchId,
        String accountId,
        Double totalAmount
) {
    /**
     * Compact constructor that validates all required fields.
     */
    public SaleCreatedEvent {
        if (saleId == null || saleId.isBlank())
            throw new IllegalArgumentException("saleId cannot be null or blank");
        if (branchId == null || branchId.isBlank())
            throw new IllegalArgumentException("branchId cannot be null or blank");
        if (accountId == null || accountId.isBlank())
            throw new IllegalArgumentException("accountId cannot be null or blank");
        if (totalAmount == null || totalAmount < 0)
            throw new IllegalArgumentException("totalAmount must be non-negative");
    }
}
