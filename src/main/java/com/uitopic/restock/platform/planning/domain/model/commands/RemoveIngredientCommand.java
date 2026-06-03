package com.uitopic.restock.platform.planning.domain.model.commands;

/**
 * Command to remove an ingredient from a
 * {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}.
 *
 * @param productId      the ID of the target {@code Product} aggregate
 * @param customSupplyId the custom supply ID of the ingredient to remove
 */
public record RemoveIngredientCommand(
        String productId,
        String customSupplyId
) {}
