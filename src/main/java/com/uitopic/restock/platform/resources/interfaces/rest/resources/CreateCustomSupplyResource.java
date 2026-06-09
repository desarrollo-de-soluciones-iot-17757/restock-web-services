package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request resource for creating a custom supply using multipart/form-data.
 *
 * The account ID is received as a query parameter.
 */
@Schema(description = "Request resource for creating a custom supply")
public record CreateCustomSupplyResource(
        @NotBlank
        @Schema(description = "Custom supply name")
        String name,

        @NotBlank
        @Schema(description = "Base supply ID")
        String supplyId,

        @NotNull
        @PositiveOrZero
        @Schema(description = "Minimum stock")
        Double minimumStock,

        @NotNull
        @PositiveOrZero
        @Schema(description = "Maximum stock")
        Double maximumStock,

        @NotBlank
        @Schema(description = "Unit price as 'amount currency', e.g. '10.00 PEN'")
        String unitPrice,

        @NotBlank
        @Schema(description = "Description")
        String description,

        @NotBlank
        @Schema(description = "Unit of measurement")
        String unitMeasurement,

        @Schema(description = "Custom supply image file")
        MultipartFile image
) {
}