package com.uitopic.restock.platform.resources.domain.model.commands;

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
