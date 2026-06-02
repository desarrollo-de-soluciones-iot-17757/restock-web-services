package com.uitopic.restock.platform.resources.domain.model.events;

import com.uitopic.restock.platform.resources.domain.model.commands.SubtractInventoryCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Domain event representing the subtraction of stock from a batch in a specific branch.
 * This event is triggered when stock is reduced due to reasons such as sales, damage, theft, or expiration. It captures the details of the stock subtraction, including the branch and batch identifiers, the quantity subtracted, the remaining stock after the subtraction, the unit of measurement, and the reason for the subtraction.
 */
@Data
@Builder
@AllArgsConstructor
public class StockSubtractedEvent {

    /**
     * The unique identifier of the branch where the stock was subtracted.
     */
    @NotBlank
    private String branchId;

    /**
     * The unique identifier of the batch for which the stock was subtracted.
     */
    @NotBlank
    private String batchId;

    /**
     * The quantity of stock that was subtracted from the batch.
     */
    @NotEmpty
    private Double quantitySubtracted;

    /**
     * The remaining quantity of stock in the inventory after the subtraction occurred.
     */
    @NotEmpty
    private Double remainingStock;

    /**
     * The unit of measurement for the quantity of stock that was subtracted (e.g., "units", "liters", "kilograms").
     */
    @NotBlank
    private String unitMeasurement;

    /**
     * Constructor to create a StockSubtractedEvent from a SubtractInventoryCommand. This constructor initializes the event's fields based on the information provided in the command, including the branch ID, batch ID, quantity subtracted, unit of measurement, and remaining stock after the subtraction.
     *
     * @param command the SubtractInventoryCommand containing the details of the stock subtraction, including the branch ID, batch ID, quantity to subtract, and unit of measurement
     * @param remainingStock the remaining quantity of stock in the inventory after the subtraction occurred, which is calculated based on the original stock level minus the quantity subtracted
     */
    public StockSubtractedEvent(SubtractInventoryCommand command, Double remainingStock) {
        this.branchId = command.branchId();
        this.batchId = command.batchId();
        this.quantitySubtracted = command.quantity().getValue();
        this.unitMeasurement = command.quantity().getUnit();
        this.remainingStock = remainingStock;
    }
}
