package com.uitopic.restock.platform.resources.domain.model.events;

/**
 * Event published when a custom supply is deleted within the resources bounded context.
 */
public record CustomSupplyDeletedEvent(String supplyId, String accountId) {}
