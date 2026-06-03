package com.uitopic.restock.platform.planning.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Response resource representing a single ingredient embedded in a
 * {@link ProductResource}.
 *
 * @param id             unique identifier of this ingredient entry
 * @param productId      ID of the owning product
 * @param customSupplyId FK to the custom supply in the {@code resources} BC
 * @param quantity       amount of the supply required per product unit
 * @param totalCost      pre-computed cost for this ingredient (quantity × unitPrice)
 */
@Schema(description = "Response resource representing an ingredient of a product")
public record IngredientResource(

        @Schema(description = "Ingredient entry ID", example = "64f1a2b3c4d5e6f7a8b9c0d4")
        String id,

        @Schema(description = "Owning product ID", example = "64f1a2b3c4d5e6f7a8b9c0d3")
        String productId,

        @Schema(description = "Custom supply ID", example = "64f1a2b3c4d5e6f7a8b9c0e2")
        String customSupplyId,

        @Schema(description = "Quantity required per product unit", example = "0.025")
        Double quantity,

        @Schema(description = "Total ingredient cost (quantity × unit price)", example = "2.50")
        BigDecimal totalCost
) {}
