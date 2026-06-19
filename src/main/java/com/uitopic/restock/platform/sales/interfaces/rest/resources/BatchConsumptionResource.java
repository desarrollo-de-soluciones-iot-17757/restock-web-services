package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resource representing the physical batch consumed")
public record BatchConsumptionResource(
        @Schema(description = "Physical batch ID in inventory", example = "b-99")
        String batchId,

        @Schema(description = "Exact quantity to consume from this batch", example = "500.0")
        Double quantityToConsume
) {}