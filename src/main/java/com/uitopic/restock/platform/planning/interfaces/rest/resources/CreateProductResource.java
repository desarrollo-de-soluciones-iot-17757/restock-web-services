package com.uitopic.restock.platform.planning.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request resource for creating a new {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}
 * within the {@code planning} bounded context.
 *
 * @param accountId    tenant account owning this product
 * @param name         human-readable product name
 * @param description  optional description of the product
 * @param sku          stock-keeping unit identifier (unique per account)
 * @param type         product type: {@code "RECIPE"} or {@code "KIT"}
 * @param imageUrl     optional URL for the product image
 * @param sellingPrice selling price of the finished product
 */
@Schema(description = "Request resource for creating a product (recipe or kit)")
public record CreateProductResource(

        @NotBlank
        @Schema(description = "Tenant account ID", example = "64f1a2b3c4d5e6f7a8b9c0d1")
        String accountId,

        @NotBlank
        @Schema(description = "Product name", example = "Café Americano")
        String name,

        @Schema(description = "Optional description", example = "Classic espresso with hot water")
        String description,

        @NotBlank
        @Schema(description = "Stock-keeping unit identifier", example = "CAF-AM-001")
        String sku,

        @NotBlank
        @Schema(description = "Product type: RECIPE or KIT", example = "RECIPE")
        String type,

        @Schema(description = "Optional image URL", example = "https://res.cloudinary.com/demo/image/upload/cafe.jpg")
        String imageUrl,

        @NotNull
        @Positive
        @Schema(description = "Selling price", example = "8.50")
        BigDecimal sellingPrice
) {}
