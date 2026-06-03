package com.uitopic.restock.platform.planning.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Request resource for adding an ingredient to an existing
 * {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}.
 *
 * <p>The {@code totalCost} is <strong>not</strong> included here — it is computed
 * server-side by the command service using the current unit price of the supply.</p>
 *
 * @param customSupplyId FK to {@code custom_supplies._id} in the {@code resources} BC
 * @param quantity       amount of the supply required for one product unit (must be positive)
 */
@Schema(description = "Request resource for adding an ingredient to a product")
public record AddIngredientResource(

        @NotBlank
        @Schema(description = "Custom supply ID from the resources bounded context",
                example = "64f1a2b3c4d5e6f7a8b9c0e2")
        String customSupplyId,

        @Positive
        @Schema(description = "Quantity of the supply needed per product unit", example = "0.025")
        Double quantity
) {}
