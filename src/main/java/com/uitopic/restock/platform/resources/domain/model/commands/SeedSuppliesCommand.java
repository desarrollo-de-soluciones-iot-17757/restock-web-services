package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to seed a new supply template within the resources bounded context.
 */
public record SeedSuppliesCommand(
        String name,
        String description,
        String category,
        boolean isPerishable
) {}
