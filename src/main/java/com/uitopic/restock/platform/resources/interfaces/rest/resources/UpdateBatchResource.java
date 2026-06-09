package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Request resource for partially updating a batch.
 *
 * Branch and custom supply cannot be changed from this endpoint.
 */
@Schema(description = "Request resource for partially updating a batch")
public record UpdateBatchResource(
        @Schema(description = "Batch code")
        String code,

        @PositiveOrZero
        @Schema(description = "Current stock quantity")
        Double currentStock,

        @Schema(description = "Expiration date in ISO format, e.g. 2026-12-31")
        String expirationDate
) {
}