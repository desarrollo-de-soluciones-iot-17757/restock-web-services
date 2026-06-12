package com.uitopic.restock.platform.resources.domain.model.events;

import com.uitopic.restock.platform.shared.domain.model.events.NotificationEvent;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.StockAlertLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class InventoryBelowMinimumStockEvent implements NotificationEvent {

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

    /**
     * The alert level indicating the severity of the stock situation, which can be used to determine the appropriate actions to take (e.g., restocking, sending notifications). This field is essential for categorizing the inventory event and ensuring that the right level of attention is given to the issue based on its severity.
     */
    @NotNull
    private StockAlertLevel alertLevel;

    /**
     * The title of the notification, which is dynamically generated based on the alert level and branch name. This title provides a clear indication of the nature of the notification, making it easier to identify and address the issue.
      * @return String
     */
    @Override
    public String notificationTitle() {
        return alertLevel == StockAlertLevel.CRITICAL
                ? "Critical Stock in " + branchName
                : "Low Stock in " + branchName;
    }

    /**
     * The message of the notification, which provides details about the current stock level, the minimum stock level, and the batch information. This message is designed to give recipients a clear understanding of the inventory situation and the necessary actions to take.
     * @return String
     */
    @Override
    public String notificationMessage() {
        return "The bath " + batchCode + " has " + currentStock.intValue() + " stock" + ". The minimum stock is " + minimumStock.intValue() + ".";
    }
}
