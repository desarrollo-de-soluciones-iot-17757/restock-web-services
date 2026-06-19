package com.uitopic.restock.platform.tracking.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.exceptions.StockComparisonIncompletedException;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ComparisonResult;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Aggregate root representing a stock comparison task, which compares the physical stock obtained from a count against the system stock to identify discrepancies. The task is associated with a specific device that performed the stock count and tracks the result of the comparison.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StockComparisonTask extends Task {

    /**
     * The stock record obtained from the physical count of the device, which is used for comparison against the system stock. This field is set when the task is created and remains unchanged throughout the task's lifecycle.
     */
    private StockRecord physicalStock;

    /**
     * The stock record obtained from the system, which is used for comparison against the physical stock. This field is set when the task is created and remains unchanged throughout the task's lifecycle.
     */
    private StockRecord systemStock;

    /**
     * The result of the stock comparison, which can be either MATCH or MISMATCH. This field is set when the task is completed.
     */
    private ComparisonResult result;

    /**
     * Represent justified withdrawn of physical stock
     */
    private Double justifiedWithdrawnStockUsed;

    /**
     * Total physical stock used in the comparison, which includes both the physical stock obtained from the count and any justified withdrawn stock.
     */
    private Double totalPhysicalStock;

    /**
     * Represents the difference between the physical stock and the system stock. This field is set when the task is completed and indicates whether there is a discrepancy between the two stock records.
     */
    private Double difference;


    /**
     * The identifier of the account related to this stock comparison task. This field is set when the task is created and remains unchanged throughout the task's lifecycle.
     */
    private AccountId accountId;

    /**
     * The identifier of the branch related to the stock comparison task. This field is set when the task is created and remains unchanged throughout the task's lifecycle.
     */
    private BranchId branchId;

    /**
     * The identifier of the batch related to the stock comparison task. This field is set when the task is created and remains unchanged throughout the task's lifecycle.
     */
    private BatchId batchId;

    /**
     * The identifier of the custom supply related to the stock comparison task
     */
    private String customSupplyId;

    /**
     * The name of the custom supply related to the stock comparison task
     */
    private String customSupplyName;


    /**
     * Creates a new stock comparison task with the given physical and system stock records and device ID.
     *
     * @param physicalStock the stock record obtained from the physical count
     * @param systemStock the stock record obtained from the system
     * @param deviceId the unique identifier of the device that performed the stock count, provided by the request
     */
    public StockComparisonTask(
            StockRecord physicalStock,
            StockRecord systemStock,
            DeviceId deviceId,
            Double justifiedWithdrawnStockUsed,
            AccountId accountId,
            BranchId branchId,
            BatchId batchId,
            String customSupplyId,
            String customSupplyName
    ) {
        super(deviceId);

        if (physicalStock == null) {
            throw new StockComparisonIncompletedException("Device physical stock cannot be null");
        }
        if (systemStock == null) {
            throw new StockComparisonIncompletedException("System stock cannot be null");
        }
        if (justifiedWithdrawnStockUsed == null || justifiedWithdrawnStockUsed < 0.0) {
            throw new StockComparisonIncompletedException("Justified withdrawn stock cannot be null or negative");
        }
        if (accountId == null) {
            throw new StockComparisonIncompletedException("Account ID cannot be null");
        }
        if (branchId == null) {
            throw new StockComparisonIncompletedException("Branch ID cannot be null");
        }
        if (batchId == null) {
            throw new StockComparisonIncompletedException("Batch ID cannot be null");
        }
        if (customSupplyId == null || customSupplyId.isBlank()) {
            throw new StockComparisonIncompletedException("Custom supply ID cannot be null or blank");
        }
        if (customSupplyName == null || customSupplyName.isBlank()) {
            throw new StockComparisonIncompletedException("Custom supply name cannot be null or blank");
        }

        this.result = ComparisonResult.IN_PROGRESS;
        this.physicalStock = physicalStock;
        this.systemStock = systemStock;
        this.justifiedWithdrawnStockUsed = justifiedWithdrawnStockUsed;

        this.totalPhysicalStock = physicalStock.getStock() + justifiedWithdrawnStockUsed;
        this.difference = Math.abs(systemStock.getStock() - this.totalPhysicalStock);

        this.accountId = accountId;
        this.branchId = branchId;
        this.batchId = batchId;
        this.customSupplyId = customSupplyId;
        this.customSupplyName = customSupplyName;
    }

    /**
     * Checks if the difference between the digital stock and the total physical
     * stock is greater than zero.
     *
     * @return true if an anomaly is detected, false otherwise
     */
    public Boolean isAnomalyDetected() {
        return this.difference > 0;
    }

    /**
     * Marks the task as a match and completes it.
     */
    public void stockMatch() {
        this.result = ComparisonResult.MATCH;
        complete();
    }

    /**
     * Marks the task as a mismatch and completes it.
     */
    public void stockMismatch() {
        this.result = ComparisonResult.MISMATCH;
        complete();
    }
}
