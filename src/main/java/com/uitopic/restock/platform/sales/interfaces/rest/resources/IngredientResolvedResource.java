package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Resource representing a resolved ingredient and its reserved batches")
public record IngredientResolvedResource(
        @Schema(description = "Custom supply or ingredient ID", example = "sup-flour")
        String customSupplyId,

        @Schema(description = "Name of the supply/ingredient", example = "Flour")
        String name,

        @Schema(description = "Total theoretical quantity required for the item", example = "500.0")
        Double quantityRequired,

        @Schema(description = "Unit of measurement", example = "g")
        String unit,

        @Schema(description = "Physical batches reserved to supply this ingredient")
        List<BatchConsumptionResource> batchesReserved
) {}