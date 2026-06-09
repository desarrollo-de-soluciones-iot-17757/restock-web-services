package com.uitopic.restock.platform.resources.domain.model.events;

/**
 * Event published when a custom supply is deleted.
 *
 * @param customSupplyId identifier of the deleted custom supply
 * @param accountId identifier of the account that owned the custom supply
 */
public record CustomSupplyDeletedEvent(
        String customSupplyId,
        String accountId
) {
}