package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request resource to add a product to an order")
public record AddProductToOrderResource(
        @NotBlank @Schema(description = "Product ID")
        String productId,

        @NotBlank @Schema(description = "Product type (KIT or RECIPE)")
        String productType,

        @NotBlank @Schema(description = "Name snapshot")
        String nameSnapshot,

        @NotNull @Positive @Schema(description = "Unit price")
        double unitPrice,

        @NotBlank @Schema(description = "Currency")
        String currency,

        @NotNull @Positive @Schema(description = "Quantity")
        int quantity
) {}