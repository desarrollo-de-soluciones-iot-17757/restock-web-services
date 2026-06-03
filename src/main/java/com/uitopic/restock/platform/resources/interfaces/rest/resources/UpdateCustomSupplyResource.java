package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request resource for partially updating a custom supply.
 *
 * The base supply ID cannot be changed from this endpoint.
 */
@Schema(description = "Request resource for partially updating a custom supply")
public record UpdateCustomSupplyResource(
        @Schema(description = "Custom supply name")
        String name,

        @PositiveOrZero
        @Schema(description = "Minimum stock")
        Double minimumStock,

        @PositiveOrZero
        @Schema(description = "Maximum stock")
        Double maximumStock,

        @Schema(description = "Unit price as 'amount currency', e.g. '10.00 PEN'")
        String unitPrice,

        @Schema(description = "Description")
        String description,

        @Schema(description = "Unit of measurement")
        String unitMeasurement,

        @Schema(description = "Custom supply image file")
        MultipartFile image
) {
}