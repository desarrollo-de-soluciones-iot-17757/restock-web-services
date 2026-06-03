package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to transfer stock from a batch to another branch.
 */
public record TransferBatchStockCommand(
        String sourceBatchId,
        String targetBranchId,
        Double quantity,
        String reason
) {
    public TransferBatchStockCommand {
        validateText(sourceBatchId, "Source batch ID");
        validateText(targetBranchId, "Target branch ID");

        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

    private static void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}