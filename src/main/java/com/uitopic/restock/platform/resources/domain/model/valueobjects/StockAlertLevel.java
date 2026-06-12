package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Alert level produced by inventory stock threshold evaluation.
 */
public enum StockAlertLevel {
    OK,
    WARNING,
    CRITICAL;

    /**
     * Evaluates the current stock against the minimum stock level and returns the appropriate alert level.
     *
     * The evaluation is based on the following criteria:
     * - If the minimum stock level is zero or negative, it returns OK (indicating no alert).
     * - If the current stock is less than or equal to 80% of the minimum stock level, it returns CRITICAL (indicating a critical alert).
     * - If the current stock is less than or equal to the minimum stock level but greater than 80% of it, it returns WARNING (indicating a warning alert).
     * - If the current stock is greater than the minimum stock level, it returns OK (indicating no alert).
     *
     * @param currentStock The current stock level.
     * @param minimumStock The minimum stock level.
     * @return The appropriate StockAlertLevel based on the evaluation of current and minimum stock levels.
     */
    public static StockAlertLevel from(double currentStock, double minimumStock) {
        if (minimumStock <= 0) {
            return OK;
        }

        if (currentStock <= minimumStock * 0.8) {
            return CRITICAL;
        }

        if (currentStock <= minimumStock) {
            return WARNING;
        }

        return OK;
    }

    /**
     * Compares the severity of this alert level with another alert level.
     *
     * The severity is determined by the ordinal value of the enum constants, where OK is the least severe and CRITICAL is the most severe.
     *
     * @param other The other StockAlertLevel to compare against.
     * @return true if this alert level is more severe than the other alert level, false otherwise.
     */
    public boolean isMoreSevereThan(StockAlertLevel other) {
        return this.ordinal() > other.ordinal();
    }
}
