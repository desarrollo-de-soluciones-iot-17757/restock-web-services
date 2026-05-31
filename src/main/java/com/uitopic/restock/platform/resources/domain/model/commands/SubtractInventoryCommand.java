package com.uitopic.restock.platform.resources.domain.model.commands;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;

/**
 * Command to subtract inventory from a specific batch in a branch.
 *
 * @param branchId the ID of the branch where the inventory is located
 * @param batchId the ID of the batch from which inventory will be subtracted
 * @param quantity the quantity of inventory to subtract
 * @param reason the reason for subtracting inventory (e.g., damage, loss, etc.)
 */
public record SubtractInventoryCommand(
        String branchId,
        String batchId,
        Stock quantity,
        String reason
) {}