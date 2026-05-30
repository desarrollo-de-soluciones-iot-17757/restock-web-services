package com.uitopic.restock.platform.resources.domain.model.commands;

public record CreateBatchCommand(
        String accountId,
        String branchId,
        String customSupplyId,
        double currentQuantity,
        String unit,
        String expirationDate
) {}
