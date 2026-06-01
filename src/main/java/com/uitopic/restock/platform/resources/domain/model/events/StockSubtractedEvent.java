package com.uitopic.restock.platform.resources.domain.model.events;

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
}
