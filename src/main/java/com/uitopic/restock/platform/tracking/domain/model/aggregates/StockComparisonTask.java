package com.uitopic.restock.platform.tracking.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
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
@Setter
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
     * Creates a new stock comparison task with the given physical and system stock records and device ID.
     *
     * @param physicalStock the stock record obtained from the physical count
     * @param systemStock the stock record obtained from the system
     * @param deviceId the unique identifier of the device that performed the stock count, provided by the request
     */
    public StockComparisonTask(
            StockRecord physicalStock,
            StockRecord systemStock,
            DeviceId deviceId
    ) {
        super(deviceId);
        this.result = ComparisonResult.IN_PROGRESS;
        this.physicalStock = physicalStock;
        this.systemStock = systemStock;
    }

    /**
     * Checks if the stock levels are within the specified threshold.
     *
     * @param threshold the acceptable difference between physical and system stock levels
     * @return true if an anomaly is detected (i.e., the difference exceeds the threshold), false otherwise
     */
    public Boolean isAnomalyDetected(Integer threshold) {
        return !systemStock.isInThreshold(physicalStock, threshold);
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
