package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Resource representing a sold product within the order")
public record SalesOrderItemResource(

        @Schema(description = "Item ID", example = "item-uuid-123")
        String id,

        @Schema(description = "Product ID (Recipe, Kit or Custom Supply)", example = "prod-uuid-123")
        String productId,

        @Schema(description = "Product type (KIT, RECIPE or SUPPLY)", example = "KIT")
        String productType,

        @Schema(description = "Name snapshot at time of sale", example = "papa y leche")
        String nameSnapshot,

        @Schema(description = "Unit price at time of sale", example = "25.00")
        Double unitPrice,

        @Schema(description = "Requested quantity", example = "2")
        int quantity,

        @Schema(description = "Batches consumed to supply this item")
        List<IngredientResolvedResource> ingredientsResolved
) {}