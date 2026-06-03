package com.uitopic.restock.platform.planning.domain.model.commands;

/**
 * Command to add an ingredient (component) to an existing
 * {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}.
 *
 * <p>The {@code totalCost} is <strong>not</strong> supplied by the caller — it is computed
 * by the application service by fetching the current {@code unitPrice} of the referenced
 * {@code customSupplyId} via the pricing port and multiplying by {@code quantity}.</p>
 *
 * @param productId      the ID of the target {@code Product} aggregate
 * @param customSupplyId FK to {@code custom_supplies._id} in the {@code resources} BC
 * @param quantity       amount of the supply needed for one product unit (must be positive)
 */
public record AddIngredientCommand(
        String productId,
        String customSupplyId,
        Double quantity
) {}
