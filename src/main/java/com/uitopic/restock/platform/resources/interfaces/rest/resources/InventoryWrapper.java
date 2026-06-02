package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Response resource representing a wrapper for a list of inventory items associated with a branch
 *
 * @param branchId Unique identifier of the branch
 * @param totalInventoryItems Total number of inventory items
 * @param inventories List of inventory items associated with the branch
 */
@Schema(description = "Wrapper for a list of inventory items associated with a branch")
public record InventoryWrapper(
        @Schema(description = "Unique identifier of the branch", example = "123e4567-e89b-12d3-a456-426614174000")
        String branchId,
        @Schema(description = "Total number of inventory items", example = "10")
        int totalInventoryItems,
        @Schema(description = "List of inventory items")
        List<InventoryResource> inventories
) {
}
