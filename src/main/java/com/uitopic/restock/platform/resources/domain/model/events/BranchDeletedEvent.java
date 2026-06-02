package com.uitopic.restock.platform.resources.domain.model.events;

/**
 * Event published when a branch is deleted or deactivated.
 *
 * @param branchId deleted branch identifier
 * @param accountId owner account identifier
 */
public record BranchDeletedEvent(
        String branchId,
        String accountId
) {
}