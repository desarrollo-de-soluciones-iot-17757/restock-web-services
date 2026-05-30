package com.uitopic.restock.platform.resources.domain.model.commands;

public record TransferInventoryCommand(
        String fromBranchId,
        String toBranchId,
        String customSupplyId,
        Integer quantity,
        String unit,
        String reason
) {}
