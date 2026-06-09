package com.uitopic.restock.platform.resources.domain.model.events;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Domain event representing the situation where an inventory has no stock available.
 * This event is triggered when the stock level of an inventory reaches zero or falls below the minimum stock level, indicating that the inventory is out of stock and may require restocking or other actions to replenish the stock.
 */
@Data
@AllArgsConstructor
@Builder
public class InventoryWithoutStockEvent {

    /**
     * The code of the batch for which the stock is subtracted. This field is used to identify the specific batch of inventory that is affected by the event, allowing for better tracking and management of inventory levels, especially when multiple batches are involved in the inventory management process.
     */
    @NotBlank
    private String batchCode;

    /**
     * The name of the branch where the inventory is located. This field is used to provide context about the location of the inventory, allowing for better identification and understanding of the inventory event, especially when multiple branches are involved in the inventory management process.
     */
    @NotBlank
    private String branchName;

    /**
     * The unique identifier of the branch where the inventory is located.
     */
    @NotBlank
    private String branchId;

    /**
     * The unique identifier of the batch for which the stock was subtracted.
     */
    @NotBlank
    private String batchId;

    /**
     * The unique identifier of the account associated with the inventory event. This field is used to identify which account the inventory event is related to, allowing for personalized handling and ensuring that the correct recipients receive notifications or actions based on their account association.
     */
    @NotBlank
    private String accountId;
}