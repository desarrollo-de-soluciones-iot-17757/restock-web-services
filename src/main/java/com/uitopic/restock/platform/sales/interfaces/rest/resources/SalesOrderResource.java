package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Main response resource for a Sales Order")
public record SalesOrderResource(
        @Schema(description = "Unique identifier of the order", example = "ord-123")
        String id,

        @Schema(description = "Current status of the order", example = "PENDING")
        String status,

        @Schema(description = "List of products included in the order")
        List<SalesOrderItemResource> items,

        @Schema(description = "Total accumulated amount of the order", example = "150.00")
        Double totalAmount
) {}