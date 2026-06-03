package com.uitopic.restock.platform.planning.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response resource representing a
 * {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product} aggregate.
 *
 * @param id           unique identifier of the product
 * @param accountId    tenant account that owns the product
 * @param name         human-readable product name
 * @param description  optional product description
 * @param sku          stock-keeping unit identifier
 * @param type         product type ({@code RECIPE} or {@code KIT})
 * @param status       lifecycle status ({@code ACTIVE}, {@code INACTIVE}, {@code DELETED})
 * @param imageUrl     optional product image URL
 * @param sellingPrice selling price of the finished product
 * @param ingredients  list of embedded ingredient details
 */
@Schema(description = "Response resource representing a product (recipe or kit)")
public record ProductResource(

        @Schema(description = "Product ID", example = "64f1a2b3c4d5e6f7a8b9c0d3")
        String id,

        @Schema(description = "Account ID", example = "64f1a2b3c4d5e6f7a8b9c0d1")
        String accountId,

        @Schema(description = "Product name", example = "Café Americano")
        String name,

        @Schema(description = "Product description", example = "Classic espresso with hot water")
        String description,

        @Schema(description = "SKU", example = "CAF-AM-001")
        String sku,

        @Schema(description = "Product type", example = "RECIPE")
        String type,

        @Schema(description = "Image URL", example = "https://res.cloudinary.com/demo/image/upload/cafe.jpg")
        String imageUrl,

        @Schema(description = "Selling price", example = "8.50")
        BigDecimal sellingPrice,

        @Schema(description = "List of ingredients")
        List<IngredientResource> ingredients
) {}
