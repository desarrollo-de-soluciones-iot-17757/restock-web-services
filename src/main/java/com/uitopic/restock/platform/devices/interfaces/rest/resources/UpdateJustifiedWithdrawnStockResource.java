package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Request body to update the justified withdrawn stock for a device.")
public record UpdateJustifiedWithdrawnStockResource(

        @Schema(description = "Quantity of stock withdrawn physically but not via sales orders")
        @NotNull(message = "Amount is required")
        @PositiveOrZero(message = "Amount must be zero or positive")
        Double amount
) {
}
