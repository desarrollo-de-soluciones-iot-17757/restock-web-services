package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to create a new custom supply within the resources bounded context.
 */
public record CreateCustomSupplyCommand(
        String accountId,
        String supplyId,
        String name,
        String description,
        String unitPrice,
        double supplyContent,
        String unitMeasurement,
        int minimumStock,
        String imageUrl
) {}
