package com.uitopic.restock.platform.resources.domain.model.events;

/**
 * Event published when stock is increased within the resources bounded context.
 */
public record StockIncreasedEvent(String batchId, String branchId, String supplyId, double quantity) {}
