package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Value object representing the minimum stock threshold for a {@link com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply}.
 *
 * <p>When the current stock of a supply falls below this threshold, the inventory state
 * transitions to {@link com.uitopic.restock.platform.resources.domain.model.valueobjects.InventoryState#LOWSTOCK}.
 * This value object enforces that the minimum stock is always a non-negative integer.
 *
 * @param minimumStock the minimum stock level, must be a non-negative integer
 */
public record MinimumStock(
        Integer minimumStock
) {

    /**
     * Compact constructor that validates the minimum stock value.
     *
     * @throws IllegalArgumentException if {@code minimumStock} is null or negative
     */
    public MinimumStock {
        if (minimumStock == null || minimumStock < 0) {
            throw new IllegalArgumentException("Minimum stock must be a non-negative integer.");
        }
    }

    /**
     * Checks if the given stock level is below the minimum stock threshold.
     *
     * @param stock the current stock level to compare against the minimum
     * @return {@code true} if {@code stock} is below the minimum stock level, {@code false} otherwise
     * @throws IllegalArgumentException if {@code stock} is negative
     */
    public boolean isStockBelowMinimum(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Current stock must be a non-negative integer.");
        }
        return stock < minimumStock;
    }

    /**
     * Returns the minimum stock threshold value.
     *
     * @return the minimum stock as an {@link Integer}
     */
    public Integer getMinimumStock() {
        return minimumStock;
    }
}
