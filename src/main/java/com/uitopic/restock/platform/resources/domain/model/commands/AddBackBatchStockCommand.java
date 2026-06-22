package com.uitopic.restock.platform.resources.domain.model.commands;

public record AddBackBatchStockCommand(
        String batchId,
        Double quantity
) {
    public AddBackBatchStockCommand {
        if (batchId == null || batchId.isBlank()) {
            throw new IllegalArgumentException("Batch ID cannot be null or blank");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
}