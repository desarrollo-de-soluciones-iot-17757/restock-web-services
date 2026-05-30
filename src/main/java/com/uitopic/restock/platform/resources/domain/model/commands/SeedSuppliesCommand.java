package com.uitopic.restock.platform.resources.domain.model.commands;

public record SeedSuppliesCommand(
        String name,
        String description,
        String category,
        boolean isPerishable
) {}
