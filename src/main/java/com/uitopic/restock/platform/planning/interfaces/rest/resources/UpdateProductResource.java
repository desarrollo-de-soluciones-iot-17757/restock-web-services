package com.uitopic.restock.platform.planning.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Request resource for updating an existing
 * {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}.
 *
 * <p>All fields are optional. A {@code null} value means "leave the current value unchanged".
 * At least one non-null field should be provided for the request to be meaningful.</p>
 *
 * @param name         new product name, or {@code null} to keep current
 * @param description  new description, or {@code null} to keep current
 * @param sku          new SKU (uniqueness re-validated if changed), or {@code null} to keep current
 * @param imageUrl     new image URL, or {@code null} to keep current
 * @param sellingPrice new selling price (must be positive if provided), or {@code null} to keep current
 */
@Schema(description = "Request resource for updating a product. All fields are optional.")
public record UpdateProductResource(

        @Schema(description = "New product name", example = "Café Latte")
        String name,

        @Schema(description = "New description", example = "Espresso with steamed milk")
        String description,

        @Schema(description = "New SKU (must be unique per account)", example = "CAF-LA-002")
        String sku,

        @Schema(description = "New image URL", example = "https://res.cloudinary.com/demo/image/upload/latte.jpg")
        String imageUrl,

        @Schema(description = "New selling price", example = "9.50")
        BigDecimal sellingPrice
) {}
