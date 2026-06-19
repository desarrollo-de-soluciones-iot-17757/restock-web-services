package com.uitopic.restock.platform.tracking.domain.model.valueobjects;

/**
 * Business reason used to explain why a discrepancy was resolved.
 */
public enum ConciliationResolutionReason {
    /**
     * Stock was discarded because it expired, spoiled, or became unusable.
     */
    WASTE_OR_SPOILAGE,

    /**
     * Stock is missing due to theft, loss, or an unexplained shortage.
     */
    THEFT_OR_LOSS,

    /**
     * Stock was consumed operationally without being registered in the system.
     */
    UNREGISTERED_USE,

    /**
     * Stock was moved to a display, counter, kitchen or preparation area while
     * still existing physically in the operation.
     */
    TRANSFER_OR_DISPLAY,

    /**
     * The discrepancy was caused by an incorrect device or sensor reading.
     */
    SENSOR_FAULT,

    /**
     * A later telemetry reading showed that the stock comparison is balanced.
     */
    STOCK_NORMALIZED
}
