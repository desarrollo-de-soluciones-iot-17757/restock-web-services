package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response resource representing a custom supply.
 */
@Schema(description = "Response resource representing a custom supply")
public record CustomSupplyResource(

        @Schema(description = "Custom supply ID")
        String id,

        @Schema(description = "Custom supply name")
        String name,

        @Schema(description = "Custom supply description")
        String description,

        @Schema(description = "Base supply ID")
        String supplyId,

        @Schema(description = "Base supply name")
        String supplyName,

        @Schema(description = "Base supply category")
        String categoryName,

        @Schema(description = "Unit price amount")
        String unitPriceAmount,

        @Schema(description = "Unit price currency code")
        String unitPriceCurrencyCode,

        @Schema(description = "Unit of measurement")
        String unitMeasurement,

        @Schema(description = "Minimum stock")
        Double minimumStock,

        @Schema(description = "Maximum stock")
        Double maximumStock,

        @Schema(description = "Image URL")
        String pictureUrl,

        @Schema(description = "Cloudinary public ID")
        String picturePublicId,

        @Schema(description = "Account ID")
        String accountId,

        @Schema(description = "Creation timestamp")
        String createdAt
) {
}