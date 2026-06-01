package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/** Request resource for creating a supply template within the resources bounded context. */
@Schema(description = "Request resource for creating a supply template")
public record CreateSupplyResource(
        @NotBlank @Schema(description = "Supply name") String name,
        @Schema(description = "Supply description") String description,
        @Schema(description = "Category (FOOD, BEVERAGES, CLEANING, OFFICE_SUPPLIES, MEDICAL, ELECTRONICS, PACKAGING, OTHER)") String category,
        @Schema(description = "Whether the supply is perishable") boolean isPerishable
) {}
