package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Main response resource for a Sales Order")
public record SalesOrderResource(
        @Schema(description = "Unique identifier of the order", example = "ord-123")
        String id,

        @Schema(description = "Branch ID the order belongs to", example = "branch-uuid-123")
        String branchId,

        @Schema(description = "Current status of the order", example = "PENDING")
        String status,

        @Schema(description = "List of products included in the order")
        List<SalesOrderItemResource> items,

        @Schema(description = "Subtotal amount before taxes", example = "100.00")
        Double subtotalAmount,

        @Schema(description = "Tax amount (18%)", example = "18.00")
        Double taxAmount,

        @Schema(description = "Total accumulated amount of the order", example = "118.00")
        Double totalAmount,

        @Schema(description = "Currency code", example = "PEN")
        String currency,

        @Schema(description = "Creation timestamp of the order")
        LocalDateTime createdAt
) {}