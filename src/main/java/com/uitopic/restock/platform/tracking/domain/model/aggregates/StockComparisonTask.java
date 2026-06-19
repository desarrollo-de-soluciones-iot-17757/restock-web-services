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
import lombok.Setter;

/**
 * Aggregate root representing a stock comparison task, which compares the physical stock obtained from a count against the system stock to identify discrepancies. The task is associated with a specific device that performed the stock count and tracks the result of the comparison.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class StockComparisonTask extends Task {

    /**
     * The stock record obtained from the physical count, which is used for comparison against the system stock. This field is set when the task is created and remains unchanged throughout the task's lifecycle.
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
     * The threshold used for determining whether the stock comparison result is a match or mismatch. This field is set when the task is created and remains unchanged throughout the task's lifecycle.
     */
    private Double thresholdUsed;

    /**
     * The difference gotten from the comparison, which is calculated as the absolute difference between the physical stock and system stock. This field is set when the task is completed.
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
            Double thresholdUsed,
            AccountId accountId,
            BranchId branchId,
            BatchId batchId,
            String customSupplyId,
            String customSupplyName
    ) {
        super(deviceId);

        if (physicalStock == null) {
            throw new StockComparisonIncompletedException("Physical stock cannot be null");
        }
        if (systemStock == null) {
            throw new StockComparisonIncompletedException("System stock cannot be null");
        }
        if (thresholdUsed == null || thresholdUsed < 0.0) {
            throw new StockComparisonIncompletedException("Threshold used cannot be null or negative");
        }
        if (accountId == null) {
            throw new StockComparisonIncompletedException("Account ID cannot be null");
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

        if (branchId == null) {
            throw new StockComparisonIncompletedException("Branch ID cannot be null");
        }

        this.result = ComparisonResult.IN_PROGRESS;
        this.physicalStock = physicalStock;
        this.systemStock = systemStock;
        this.thresholdUsed = thresholdUsed;
        this.difference = Math.abs(systemStock.getStock() - physicalStock.getStock());
        this.accountId = accountId;
        this.branchId = branchId;
        this.batchId = batchId;
        this.customSupplyId = customSupplyId;
        this.customSupplyName = customSupplyName;
    }

    /**
     * Checks if the stock levels are within the specified threshold.
     *
     * @return true if an anomaly is detected (i.e., the difference exceeds the threshold), false otherwise
     */
    public Boolean isAnomalyDetected() {
        return !systemStock.isInThreshold(physicalStock, thresholdUsed);
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
