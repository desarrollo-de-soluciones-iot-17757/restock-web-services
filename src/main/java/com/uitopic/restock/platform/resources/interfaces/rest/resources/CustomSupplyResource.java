package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response resource representing a custom supply.
 *
 * Includes the custom supply data and its related base supply.
 */
@Schema(description = "Response resource representing a custom supply")
public record CustomSupplyResource(
        @Schema(description = "Unique identifier of the custom supply")
        String id,

        @Schema(description = "Custom supply name")
        String name,

        @Schema(description = "Custom supply description")
        String description,

        @Schema(description = "Custom supply unit price amount")
        String unitPriceAmount,

        @Schema(description = "Custom supply unit price currency code")
        String unitPriceCurrencyCode,

        @Schema(description = "Minimum stock")
        Double minimumStock,

        @Schema(description = "Maximum stock")
        Double maximumStock,

        @Schema(description = "Unit of measurement")
        String unitMeasurement,

        @Schema(description = "Unit of measurement abbreviation")
        String unitMeasurementAbbreviation,

        @Schema(description = "Custom supply picture URL")
        String pictureUrl,

        @Schema(description = "Custom supply picture public ID")
        String picturePublicId,

        @Schema(description = "Account ID")
        String accountId,

        @Schema(description = "Base supply information")
        SupplyResource supply
) {
}