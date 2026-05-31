package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response resource representing a custom supply.
 *
 * @param id Unique identifier of the custom supply
 * @param name Name of the custom supply
 * @param description Detailed description of the custom supply
 * @param categoryName Category name of the custom supply
 * @param unitPriceAmount Price of the custom supply
 * @param unitPriceCurrencyCode Currency code for the unit price
 * @param supplyContent Content amount of the custom supply
 * @param unitMeasurement Unit of measurement for the supply content
 * @param minimumStock Minimum stock level for the custom supply
 * @param pictureUrl URL of the custom supply's picture
 * @param accountId Unique identifier of the account associated with the custom supply
 */
@Schema(description = "Response resource representing a custom supply")
public record CustomSupplyResource(
        @Schema(description = "Unique identifier of the custom supply", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
        @Schema(description = "Name of the custom supply", example = "Premium Coffee Beans")
        String name,
        @Schema(description = "Detailed description of the custom supply", example = "High-quality Arabica coffee beans sourced from Colombia.")
        String description,
        @Schema(description = "Category name of the custom supply", example = "Beverages")
        String categoryName,
        @Schema(description = "Price of the custom supply", example = "19.99")
        String unitPriceAmount,
        @Schema(description = "Currency code for the unit price", example = "PEN")
        String unitPriceCurrencyCode,
        @Schema(description = "Content amount of the custom supply", example = "500")
        double supplyContent,
        @Schema(description = "Unit of measurement for the supply content", example = "grams")
        String unitMeasurement,
        @Schema(description = "URL of the custom supply's picture", example = "https://example.com/images/premium-coffee-beans.jpg")
        String pictureUrl,
        @Schema(description = "Unique identifier of the account associated with the custom supply", example = "123e4567-e89b-12d3-a456-426614174000")
        String accountId
) {
}
