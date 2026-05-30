package com.uitopic.restock.platform.resources.domain.model.events;

public record StockTransferredEvent(String transferId, String fromBranchId, String toBranchId, String supplyId, double quantity) {}
