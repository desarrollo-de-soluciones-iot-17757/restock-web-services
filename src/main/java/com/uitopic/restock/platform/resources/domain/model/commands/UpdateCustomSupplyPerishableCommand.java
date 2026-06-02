package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to update the perishable status of an existing custom supply.
 *
 * @param id           the unique identifier of the custom supply to update
 * @param isPerishable {@code true} if the supply is perishable, {@code false} otherwise
 */
public record UpdateCustomSupplyPerishableCommand(
        String id,
        boolean isPerishable
) {
}
