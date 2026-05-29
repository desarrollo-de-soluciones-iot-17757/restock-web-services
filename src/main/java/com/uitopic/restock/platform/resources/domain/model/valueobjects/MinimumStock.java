package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Value object representing the minimum stock level for a supply item.
 *
 * @param minimumStock the minimum stock level, must be a non-negative integer
 */
public record MinimumStock(
        Integer minimumStock
) {

    // Constructor validation to ensure minimumStock is a non-negative integer
    public MinimumStock {
        if (minimumStock == null || minimumStock < 0) {
            throw new IllegalArgumentException("Minimum stock must be a non-negative integer.");
        }
    }

    /**
     * Checks if the current stock is below or at the minimum stock level.
     *
     * @param currentStock the current stock level to compare against the minimum stock
     * @return true if the current stock is below or at the minimum stock level, false otherwise
     */
    public Boolean isStockBelowOrAtMinimum(Integer currentStock) {
        if (currentStock == null || currentStock < 0) {
            throw new IllegalArgumentException("Current stock must be a non-negative integer.");
        }
        return currentStock <= minimumStock;
    }

    /**
     * Gets the minimum stock value.
     *
     * @return the minimum stock value
     */
    public Integer getMinimumStock() {
        return minimumStock;
    }
}
