package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to create a new batch within the resources bounded context.
 */
public record CreateBatchCommand(
        String accountId,
        String branchId,
        String customSupplyId,
        double currentQuantity,
        String unit,
        String expirationDate
) {}
