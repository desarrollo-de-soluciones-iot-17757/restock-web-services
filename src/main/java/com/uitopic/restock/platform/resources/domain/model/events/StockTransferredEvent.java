package com.uitopic.restock.platform.resources.domain.model.events;

import com.uitopic.restock.platform.resources.domain.model.commands.TransferBatchStockCommand;
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
    private String fromBatchId;

    /**
     * The new amount of stock available at the source branch after the transfer has been completed. This field indicates the updated inventory level at the originating branch, reflecting the quantity of stock that was transferred to the destination branch.
     */
    @NotEmpty
    private Double fromBranchRemainingStock;

    /**
     * The unique identifier of the destination branch to which the stock was transferred. This field indicates the branch that received the transferred stock, allowing for tracking and inventory management across locations.
     */
    @NotBlank
    private String toBranchId;

    /**
     * The new amount of stock available at the destination branch after the transfer has been completed. This field indicates the updated inventory level at the receiving branch, reflecting the quantity of stock that was transferred from the source branch.
     */
    @NotEmpty
    private Double toBranchRemainingStock;

    /**
     * The quantity of stock that was transferred from one branch to another. This field indicates the amount of stock that was moved between branches, providing insight into inventory changes across locations.
     */
    @NotEmpty
    private Double quantityTransferred;

    /**
     * The unit of measurement for the quantity of stock that was transferred (e.g., "units", "liters", "kilograms"). This field indicates the unit in which the quantity is measured, providing clarity on the amount of stock that was moved between branches.
     */
    @NotBlank
    private String unitMeasurement;

    /**
     * Constructor to create a StockTransferredEvent based on the provided TransferInventoryCommand and the remaining stock at both branches after the transfer. This constructor initializes the event with relevant information from the command and the updated inventory levels, allowing for accurate tracking of stock movements across branches.
     *
     * @param command the command containing the details of the inventory transfer, including source and destination branch IDs, batch ID, quantity transferred, and unit of measurement
     * @param fromBatchRemainingStock the new amount of stock available at the source branch after the transfer has been completed
     * @param toBranchRemainingStock the new amount of stock available at the destination branch after the transfer has been completed
     */
    public StockTransferredEvent(TransferBatchStockCommand command, Double fromBatchRemainingStock, Double toBranchRemainingStock) {
        this.fromBatchId = command.sourceBatchId();
        this.toBranchId = command.targetBranchId();
        this.quantityTransferred = command.quantity().stock();
        this.unitMeasurement = command.quantity().getUnit();
        this.fromBranchRemainingStock = fromBatchRemainingStock;
        this.toBranchRemainingStock = toBranchRemainingStock;
    }
}