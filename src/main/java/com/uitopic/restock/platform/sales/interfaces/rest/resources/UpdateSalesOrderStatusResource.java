package com.uitopic.restock.platform.sales.interfaces.rest.resources;

import com.uitopic.restock.platform.sales.domain.model.valueobjects.SalesOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Request resource for updating a sales order status.
 *
 * Only {@code COMPLETED} and {@code CANCELLED} are accepted transitions
 * through this endpoint; each triggers its own domain command under the hood
 * (completing consumes physical stock, cancelling does not).
 */
@Schema(description = "Request resource for updating a sales order status")
public record UpdateSalesOrderStatusResource(
        @NotNull
        @Schema(description = "Target sales order status", example = "COMPLETED")
        SalesOrderStatus status
) {
}
