package com.uitopic.restock.platform.resources.domain.model.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Domain event representing the inventory level of a batch in a specific branch falling below the minimum stock level.
 * This event is triggered when the current stock of a batch is reduced to a level that is below the defined minimum stock level for that batch. It captures the details of the inventory level, including the branch and batch identifiers, the current stock, the minimum stock level, and the unit of measurement. This event can be used for inventory management and restocking purposes to ensure that appropriate actions are taken to replenish stock levels and avoid stockouts.
 */
@Data
@AllArgsConstructor
@Builder
public class InventoryBelowMinimumStockEvent {

    /**
     * The name of the branch where the inventory is located. This provides context about the location of the inventory that is below the minimum stock level, allowing for better identification and management of the issue.
     */
    @NotBlank
    private String branchName;

    /**
     * The unique identifier of the branch where the inventory is located.
     */
    @NotBlank
    private String branchId;

    /**
     * The name of the batch for which the stock was subtracted. This provides context about the specific batch that is experiencing low inventory levels, allowing for better identification and management of the issue.
     */
    @NotBlank
    private String batchCode;

    /**
     * The unique identifier of the batch for which the stock was subtracted.
     */
    @NotBlank
    private String batchId;

    /**
     * The quantity of stock that was subtracted from the batch.
     */
    @NotEmpty
    private Double currentStock;

    /**
     * The minimum stock level for this custom supply, which can be used for inventory management and restocking purposes.
     */
    @NotEmpty
    private Double minimumStock;

    /**
     * The unit of measurement for the quantity of stock that was subtracted (e.g., "units", "liters", "kilograms").
     */
    @NotBlank
    private String unitMeasurement;

    /**
     * The account ID associated with the inventory event, represented as a string. This field is used to identify which account the inventory event is related to, allowing for personalized notifications and ensuring that the correct recipients receive the notification based on their account association.
     */
    @NotBlank
    private String accountId;
}
