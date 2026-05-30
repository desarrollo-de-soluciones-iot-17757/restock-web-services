package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request resource for creating a custom supply")
public record CreateCustomSupplyResource(
        @NotBlank @Schema(description = "Account ID") String accountId,
        @NotBlank @Schema(description = "Supply template ID") String supplyId,
        @NotBlank @Schema(description = "Name") String name,
        @Schema(description = "Description") String description,
        @NotBlank @Schema(description = "Unit price as 'amount currency', e.g. '10.00 PEN'") String unitPrice,
        @Schema(description = "Supply content (quantity per unit)") double supplyContent,
        @NotBlank @Schema(description = "Unit of measurement") String unitMeasurement,
        @Schema(description = "Minimum stock level") int minimumStock,
        @Schema(description = "Image URL") String imageUrl
) {}
