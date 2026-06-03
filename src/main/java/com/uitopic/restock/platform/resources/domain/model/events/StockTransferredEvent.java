package com.uitopic.restock.platform.resources.domain.model.events;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;

/**
 * Event published when stock is transferred between branches.
 *
 * @param sourceBatchId source batch identifier
 * @param targetBatchId target batch identifier
 * @param sourceBranchId source branch identifier
 * @param targetBranchId target branch identifier
 * @param customSupplyId custom supply associated with the transferred stock
 * @param accountId account that owns the transfer
 * @param quantity transferred quantity
 * @param reason transfer reason
 */
public record StockTransferredEvent(
        String sourceBatchId,
        String targetBatchId,
        String sourceBranchId,
        String targetBranchId,
        String customSupplyId,
        String accountId,
        Stock quantity,
        String reason
) {
}