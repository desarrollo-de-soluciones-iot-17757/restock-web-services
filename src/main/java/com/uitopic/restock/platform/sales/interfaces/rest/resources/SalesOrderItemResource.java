package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Resource representing a sold product within the order")
public record SalesOrderItemResource(

        @Schema(description = "Product ID (Recipe or Kit)", example = "prod-uuid-123")
        String productId,

        @Schema(description = "Requested quantity", example = "2")
        int quantity,

        @Schema(description = "Batches consumed to supply this item")
        List<IngredientResolvedResource> ingredientsResolved
) {}