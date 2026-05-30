package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response resource representing a supply template")
public record SupplyResource(
        @Schema(description = "Unique identifier") String id,
        @Schema(description = "Supply name") String name,
        @Schema(description = "Supply description") String description,
        @Schema(description = "Category") String category,
        @Schema(description = "Whether the supply is perishable") boolean isPerishable,
        @Schema(description = "Creation timestamp") String createdAt
) {}
