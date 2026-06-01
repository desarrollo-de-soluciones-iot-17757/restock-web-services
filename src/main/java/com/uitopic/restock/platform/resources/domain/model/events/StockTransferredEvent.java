package com.uitopic.restock.platform.resources.domain.model.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Domain event representing the transfer of stock from one branch to another. This event contains information about the source and destination branches, the batch of stock being transferred, the quantity transferred, and the remaining stock at both branches after the transfer is completed.
 * It is used to track inventory movements across different locations and ensure accurate inventory management.
 */
@Data
@AllArgsConstructor
@Builder
public class StockTransferredEvent {

    /**
     * The unique identifier of the source branch from which the stock was transferred. This field indicates the branch that initiated the transfer, allowing for tracking and inventory management across locations.
     */
    @NotBlank
    String fromBranchId;

    /**
     * The new amount of stock available at the source branch after the transfer has been completed. This field indicates the updated inventory level at the originating branch, reflecting the quantity of stock that was transferred to the destination branch.
     */
    @NotEmpty
    Double fromBranchRemainingStock;

    /**
     * The unique identifier of the destination branch to which the stock was transferred. This field indicates the branch that received the transferred stock, allowing for tracking and inventory management across locations.
     */
    @NotBlank
    String toBranchId;

    /**
     * The new amount of stock available at the destination branch after the transfer has been completed. This field indicates the updated inventory level at the receiving branch, reflecting the quantity of stock that was transferred from the source branch.
     */
    @NotEmpty
    Double toBranchRemainingStock;

    /**
     * The unique identifier of the batch for which the stock was transferred. This field indicates the specific batch of stock that was moved between branches, allowing for tracking and inventory management across locations.
     */
    @NotBlank
    String batchId;

    /**
     * The quantity of stock that was transferred from one branch to another. This field indicates the amount of stock that was moved between branches, providing insight into inventory changes across locations.
     */
    @NotEmpty
    Double quantityTransferred;

    /**
     * The unit of measurement for the quantity of stock that was transferred (e.g., "units", "liters", "kilograms"). This field indicates the unit in which the quantity is measured, providing clarity on the amount of stock that was moved between branches.
     */
    @NotBlank
    String unitMeasurement;
}
