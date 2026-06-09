package com.uitopic.restock.platform.resources.domain.model.commands;

public record CreateSupplyCommand(
        String name, String description, Boolean isPerishable, String category
) {
}
