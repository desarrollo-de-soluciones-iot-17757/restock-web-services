package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to delete an existing custom supply within the resources bounded context.
 */
public record DeleteCustomSupplyCommand(
        String customSupplyId
) {
    public DeleteCustomSupplyCommand {
        if (customSupplyId == null || customSupplyId.isBlank()) {
            throw new IllegalArgumentException("Custom supply ID cannot be null or blank");
        }
    }
}