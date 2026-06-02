package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response resource representing a custom supply within the resources bounded context.
 *
 * <p>Exposes the Supply as a primitive nested object (SupplyDto) to provide complete
 * supply information to clients without treating it as a subdocument.
 *
 * @param id Unique identifier of the custom supply
 * @param name Name of the custom supply
 * @param description Detailed description of the custom supply
 * @param category The supply template as a primitive nested object
 * @param unitPriceAmount Price of the custom supply
 * @param unitPriceCurrencyCode Currency code for the unit price
 * @param supplyContent Content amount of the custom supply
 * @param unitMeasurement Unit of measurement for the supply content
 * @param pictureUrl URL of the custom supply's picture
 * @param accountId Unique identifier of the account associated with the custom supply
 * @param isPerishable Whether this custom supply is perishable
 */
@Schema(description = "Response resource representing a custom supply")
public record CustomSupplyResource(
        @Schema(description = "Unique identifier of the custom supply", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
        @Schema(description = "Name of the custom supply", example = "Premium Coffee Beans")
        String name,
        @Schema(description = "Detailed description of the custom supply", example = "High-quality Arabica coffee beans sourced from Colombia.")
        String description,
        @Schema(description = "The supply template (primitive nested object)")
        SupplyDto category,
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
        String accountId,
        @Schema(description = "Whether this custom supply is perishable", example = "false")
        Boolean isPerishable
) {
    /**
     * Nested record representing a Supply as a primitive object within CustomSupplyResource.
     *
     * <p>This is used to expose supply information directly without treating the Supply
     * entity as a subdocument. It contains the core properties needed by the client.
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

