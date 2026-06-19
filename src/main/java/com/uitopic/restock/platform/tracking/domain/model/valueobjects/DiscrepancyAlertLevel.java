package com.uitopic.restock.platform.tracking.domain.model.valueobjects;

/**
 * Alert level produced by inventory discrepancy evaluation.
 */
public enum DiscrepancyAlertLevel {
    OK,
    WARNING,
    CRITICAL;

    /**
     * Determines the alert level from the already calculated stock difference.
     *
     * @param difference absolute difference between digital stock and total
     *                   physical stock
     * @return CRITICAL when there is a meaningful difference, otherwise OK
     */
    public static DiscrepancyAlertLevel from(double difference) {
        return difference > 0 ? CRITICAL : OK;
    }

    /**
     * Compatibility factory that evaluates the absolute difference between two
     * stock values. The threshold parameter is ignored because discrepancy
     * evaluation no longer uses a tolerated anomaly threshold.
     *
     * @param totalPhysicalStock physical stock value
     * @param systemStock digital stock value
     * @return alert level based on the absolute difference
     */
    public static DiscrepancyAlertLevel from(double totalPhysicalStock, double systemStock) {
        return from(Math.abs(totalPhysicalStock - systemStock));
    }
}
