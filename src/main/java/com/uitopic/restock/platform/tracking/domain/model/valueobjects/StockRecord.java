package com.uitopic.restock.platform.tracking.domain.model.valueobjects;

import com.uitopic.restock.platform.tracking.domain.exceptions.StockComparisonIncompletedException;
import com.uitopic.restock.platform.tracking.domain.exceptions.TelemetryValuesException;

/**
 * Value object representing the stock level of an inventory item, used for tracking inventory discrepancies.
 *
 * @param stock the stock level of the inventory, provided by the request
 */
public record StockRecord(
        Double stock
) {

    /**
     * Creates a stock record value object.
     *
     * @param stock the stock level of the inventory, provided by the request
     */
    public StockRecord {
        if (stock == null || stock < 0) {
            throw new TelemetryValuesException("Stock cannot be null or negative");
        }
    }

    /**
     * Compares this stock record with another stock record and checks if the difference is within the specified threshold.
     *
     * @param other the other stock record to compare with
     * @param threshold the maximum allowed difference between the two stock records for them to be considered within the threshold
     * @return true if the absolute difference between the two stock records is less than or equal to the threshold, false otherwise
     */
    public Boolean isInThreshold(StockRecord other, Integer threshold) {
        if (other == null) {
            throw new StockComparisonIncompletedException("Stock to compare cannot be null");
        }
        if (threshold == null || threshold < 0) {
            throw new StockComparisonIncompletedException("Threshold cannot be null or negative");
        }
        return Math.abs(this.stock - other.stock) <= threshold;
    }

    /**
     * Returns the stock level of the inventory.
     *
     * @return the stock level of the inventory
     */
    public Double getStock() {
        return stock;
    }
}
