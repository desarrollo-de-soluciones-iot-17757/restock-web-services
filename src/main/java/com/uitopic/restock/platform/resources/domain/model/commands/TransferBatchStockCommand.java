package com.uitopic.restock.platform.resources.domain.model.commands;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;

/**
 * Command to transfer stock from a batch to another branch.
 */
public record TransferBatchStockCommand(
        String sourceBatchId,
        String targetBranchId,
        Stock quantity,
        String reason
) {
    public TransferBatchStockCommand {
        validateText(sourceBatchId, "Source batch ID");
        validateText(targetBranchId, "Target branch ID");

        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
    }

    private static void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}