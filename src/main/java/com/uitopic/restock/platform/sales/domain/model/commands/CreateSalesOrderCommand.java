package com.uitopic.restock.platform.sales.domain.model.commands;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

public record CreateSalesOrderCommand(
        AccountId accountId,
        String branchId
) {
    public CreateSalesOrderCommand {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        if (branchId == null || branchId.isBlank()) {
            throw new IllegalArgumentException("Branch ID cannot be null or blank");
        }
    }
}