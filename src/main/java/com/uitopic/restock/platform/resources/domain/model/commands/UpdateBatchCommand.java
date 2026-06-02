package com.uitopic.restock.platform.resources.domain.model.commands;

import java.time.LocalDate;

/**
 * Command to partially update an existing batch.
 *
 * Only non-null fields are applied.
 */
public record UpdateBatchCommand(
        String batchId,
        String code,
        Double currentStock,
        LocalDate expirationDate
) {
    public UpdateBatchCommand {
        if (batchId == null || batchId.isBlank()) {
            throw new IllegalArgumentException("Batch ID cannot be null or blank");
        }

        if (currentStock != null && currentStock < 0) {
            throw new IllegalArgumentException("Current stock cannot be negative");
        }
    }
}