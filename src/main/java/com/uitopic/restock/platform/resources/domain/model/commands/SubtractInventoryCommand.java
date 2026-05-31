package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to subtract inventory stock within the resources bounded context.
 */
public record SubtractInventoryCommand(
        String branchId,
        String customSupplyId,
        double quantity,
        String unit,
        String reason,
        String timestamp
) {}
