package com.uitopic.restock.platform.resources.domain.model.events;

public record StockIncreasedEvent(String batchId, String branchId, String supplyId, double quantity) {}
