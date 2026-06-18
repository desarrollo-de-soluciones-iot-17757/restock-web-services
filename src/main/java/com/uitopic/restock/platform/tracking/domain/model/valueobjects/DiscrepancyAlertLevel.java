package com.uitopic.restock.platform.tracking.domain.model.valueobjects;

/**
 * Alert level produced by inventory discrepancy evaluation.
 */
public enum DiscrepancyAlertLevel {
    OK,
    WARNING,
    CRITICAL;

    /**
     * Determines the alert level based on the difference between physical stock and system stock, using a specified threshold to evaluate the severity of the discrepancy.
     *
     * @param physicalStock the stock level obtained from the physical count, provided by the comparison task
     * @param systemStock the stock level obtained from the system, provided by the comparison task
     * @param threshold the threshold used to determine the risk level of the discrepancy, provided by the comparison task
     * @return the appropriate DiscrepancyAlertLevel based on the evaluation of the difference between physical and system stock levels against the specified threshold
     */
    public static DiscrepancyAlertLevel from(double physicalStock, double systemStock, double threshold) {

        var difference = Math.abs(physicalStock - systemStock);

        if (0 <= difference && difference <= 1) {
            return OK;
        }

        if (threshold < difference) {
            return CRITICAL;
        }

        return WARNING;

    }
}
