package com.uitopic.restock.platform.resources.domain.model.events;

/**
 * Event published when stock is transferred between branches within the resources bounded context.
 */
public record StockTransferredEvent(String transferId, String fromBranchId, String toBranchId, String supplyId, double quantity) {}
