package com.uitopic.restock.platform.resources.domain.model.events;

public record StockSubtractedEvent(String deductionId, String branchId, String supplyId, double quantity, double remainingStock) {}
