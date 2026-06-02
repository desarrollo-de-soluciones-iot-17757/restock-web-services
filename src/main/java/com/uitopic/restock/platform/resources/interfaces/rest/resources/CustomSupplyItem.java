package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response resource representing a custom supply item for the wrapper within the resources bounded context.
 *
 * <p>Exposes the Supply as a primitive nested object to ensure consistency with {@link CustomSupplyResource}.
 *
 * @param id Unique identifier of the custom supply
 * @param name Name of the custom supply
 * @param description Detailed description of the custom supply
 * @param supply The supply template as a primitive nested object
 * @param unitPriceAmount Price of the custom supply
 * @param unitPriceCurrencyCode Currency code for the unit price
 * @param supplyContent Content amount of the custom supply
 * @param unitMeasurement Unit of measurement for the supply content
 * @param pictureUrl URL of the custom supply's picture
 */
@Schema(description = "Response resource representing a custom supply item for the wrapper")
public record CustomSupplyItem(
        @Schema(description = "Unique identifier of the custom supply", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
        @Schema(description = "Name of the custom supply", example = "Premium Coffee Beans")
        String name,
        @Schema(description = "Detailed description of the custom supply", example = "High-quality Arabica coffee beans sourced from Colombia.")
        String description,
        @Schema(description = "The supply template")
        SupplyDto supply,
        @Schema(description = "Price of the custom supply", example = "19.99")
        String unitPriceAmount,
        @Schema(description = "Currency code for the unit price", example = "PEN")
        String unitPriceCurrencyCode,
        @Schema(description = "Content amount of the custom supply", example = "500")
        double supplyContent,
        @Schema(description = "Unit of measurement for the supply content", example = "grams")
        String unitMeasurement,
        @Schema(description = "URL of the custom supply's picture", example = "https://example.com/images/premium-coffee-beans.jpg")
        String pictureUrl
) {
    /**
     * Nested record representing a Supply as a primitive object within CustomSupplyItem.
     */
    @Schema(description = "Supply template information")
    public record SupplyDto(
            @Schema(description = "Unique identifier of the supply template", example = "507f1f77bcf86cd799439011")
            String id,
            @Schema(description = "Name of the supply template", example = "Coffee")
            String name,
            @Schema(description = "Description of the supply template", example = "A hot beverage made from roasted coffee beans")
            String description,
            @Schema(description = "Category type of the supply template", example = "BEVERAGE")
            String category,
            @Schema(description = "Whether the supply is perishable", example = "false")
            Boolean isPerishable
    ) {
    }
}
