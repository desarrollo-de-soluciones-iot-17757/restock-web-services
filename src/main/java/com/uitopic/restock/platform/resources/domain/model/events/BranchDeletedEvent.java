package com.uitopic.restock.platform.resources.domain.model.events;

/**
 * Event published when a branch is logically deleted within the resources bounded context.
 */
public record BranchDeletedEvent(String branchId, String accountId) {}
