package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to transfer inventory between branches within the resources bounded context.
 */
public record TransferInventoryCommand(
        String fromBranchId,
        String toBranchId,
        String customSupplyId,
        double quantity,
        String unit,
        String reason
) {}
