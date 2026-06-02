package com.uitopic.restock.platform.resources.domain.model.commands;

import java.time.LocalDate;

/**
 * Command to create a new batch.
 */
public record CreateBatchCommand(
        String code,
        Double currentStock,
        String customSupplyId,
        String branchId,
        String accountId,
        LocalDate expirationDate,
        LocalDate entryDate
) {
    public CreateBatchCommand {
        validateText(code, "Batch code");
        validateText(customSupplyId, "Custom supply ID");
        validateText(branchId, "Branch ID");
        validateText(accountId, "Account ID");

        if (currentStock == null) {
            throw new IllegalArgumentException("Current stock cannot be null");
        }

        if (currentStock < 0) {
            throw new IllegalArgumentException("Current stock cannot be negative");
        }
    }

    private static void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}