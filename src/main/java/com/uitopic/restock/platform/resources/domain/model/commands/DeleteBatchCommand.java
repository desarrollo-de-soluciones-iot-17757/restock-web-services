package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to delete a batch.
 */
public record DeleteBatchCommand(
        String batchId
) {
    public DeleteBatchCommand {
        if (batchId == null || batchId.isBlank()) {
            throw new IllegalArgumentException("Batch ID cannot be null or blank");
        }
    }
}