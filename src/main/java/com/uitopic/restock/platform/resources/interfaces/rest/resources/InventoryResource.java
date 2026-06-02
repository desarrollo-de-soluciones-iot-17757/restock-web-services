package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response representing the inventory of a product. It contains details from the custom supply and batch associated with the inventory.
 *
 * @param customSupplyName Name of the custom supply associated with the inventory.
 * @param customSupplyPictureUrl URL of the picture associated with the custom supply.
 * @param batchCode Code assigned to the batch associated with the inventory.
 * @param entryDate Entry date of the batch associated with the inventory.
 * @param expirationDate Expiration date of the batch associated with the inventory.
 * @param minimumStock Minimum stock level for the inventory.
 * @param currentStock Current stock level for the inventory.
 * @param stockUnitMeasurement Unit of measurement for the stock levels.
 * @param inventoryState State of the inventory, which can be 'OK', 'LOW_STOCK', or 'EXPIRED'.
 */
@Schema(description = "Response representing the inventory of a product. It contains details from the custom supply and batch associated with the inventory.")
public record InventoryResource(
        @Schema(description = "Name of the custom supply associated with the inventory.")
        String customSupplyName,
        @Schema(description = "URL of the picture associated with the custom supply.")
        String customSupplyPictureUrl,
        @Schema(description = "Code assigned to the batch associated with the inventory.")
        String batchCode,
        @Schema(description = "Entry date of the batch associated with the inventory.")
        String entryDate,
        @Schema(description = "Expiration date of the batch associated with the inventory.")
        String expirationDate,
        @Schema(description = "Minimum stock level for the inventory.")
        double minimumStock,
        @Schema(description = "Current stock level for the inventory.")
        double currentStock,
        @Schema(description = "Unit of measurement for the stock levels.")
        String stockUnitMeasurement,
        @Schema(description = "State of the inventory, which can be 'OK', 'LOW_STOCK', or 'EXPIRED'.")
        String inventoryState
) {
}
