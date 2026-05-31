package com.uitopic.restock.platform.resources.domain.model.commands;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;

/**
 * Command to transfer inventory from one branch to another.
 *
 * @param fromBranchId the ID of the branch from which inventory will be transferred
 * @param toBranchId the ID of the branch to which inventory will be transferred
 * @param batchId the ID of the batch being transferred
 * @param quantity the quantity of inventory to transfer
 * @param reason the reason for transferring inventory (e.g., redistribution, stock balancing, etc.)
 */
public record TransferInventoryCommand(
        String fromBranchId,
        String toBranchId,
        String batchId,
        Stock quantity,
        String reason
) {}