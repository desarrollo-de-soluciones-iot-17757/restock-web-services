package com.uitopic.restock.platform.resources.domain.model.commands;

public record SubtractInventoryCommand(
        String branchId,
        String customSupplyId,
        double quantity,
        String unit,
        String reason,
        String timestamp
) {}
