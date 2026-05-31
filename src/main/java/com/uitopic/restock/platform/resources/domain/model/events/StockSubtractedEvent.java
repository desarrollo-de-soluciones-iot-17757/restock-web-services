package com.uitopic.restock.platform.resources.domain.model.events;

/**
 * Event published when stock is subtracted within the resources bounded context.
 */
public record StockSubtractedEvent(String deductionId, String branchId, String supplyId, double quantity, double remainingStock) {}
