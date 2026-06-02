package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Value object representing the allowed stock range for a custom supply.
 *
 * @param minStock minimum allowed stock
 * @param maxStock maximum allowed stock
 */
public record StockRange(
        Double minStock,
        Double maxStock
) {

    public StockRange {
        if (minStock == null) {
            throw new IllegalArgumentException("minStock cannot be null");
        }

        if (maxStock == null) {
            throw new IllegalArgumentException("maxStock cannot be null");
        }

        if (minStock < 0) {
            throw new IllegalArgumentException("minStock cannot be negative");
        }

        if (maxStock < minStock) {
            throw new IllegalArgumentException("maxStock must be greater than or equal to minStock");
        }
    }

    /**
     * Checks whether a stock value is within the configured range.
     *
     * @param value stock value to evaluate
     * @return true if the value is within range
     */
    public boolean isInRange(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        return value >= minStock && value <= maxStock;
    }
}