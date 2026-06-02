package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request resource for the PATCH perishable-status endpoint of a custom supply.
 *
 * @param isPerishable {@code true} if the supply is perishable, {@code false} otherwise
 */
@Schema(description = "Request body to update the perishable status of a custom supply")
public record UpdateCustomSupplyPerishableResource(
        @Schema(description = "Whether the custom supply is perishable", example = "true")
        boolean isPerishable
) {
}
