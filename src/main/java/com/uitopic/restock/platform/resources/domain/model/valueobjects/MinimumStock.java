package com.uitopic.restock.platform.resources.domain.model.valueobjects;

import com.uitopic.restock.platform.resources.domain.exception.InvalidMinimumStockFormatException;

/**
 * Value object representing the minimum stock level for a supply item.
 *
 * @param minimumStock the minimum stock level, must be a non-negative value
 * @param unitMeasurement the unit of measurement for the minimum stock (e.g., "pieces", "boxes", "liters", etc.)
 */
public record MinimumStock(
        Double minimumStock,
        String unitMeasurement
) {

    // Constructor validation to ensure minimumStock is a non-negative integer
    public MinimumStock {
        if (minimumStock == null || minimumStock < 0) {
            throw new InvalidMinimumStockFormatException("Minimum stock must be a non-negative integer.");
        }

        if (unitMeasurement == null || unitMeasurement.isBlank()) {
            throw new IllegalArgumentException("Unit of measurement must be a non-empty string.");
        }
    }

    /**
     * Checks if the current stock is below or at the minimum stock level.
     *
     * @param currentStock the current stock level to compare against the minimum stock
     * @return true if the current stock is below or at the minimum stock level, false otherwise
     */
    public Boolean isStockBelowOrAtMinimum(Double currentStock) {
        if (currentStock == null || currentStock < 0) {
            throw new InvalidMinimumStockFormatException("Current stock must be a non-negative integer.");
        }
        return currentStock <= minimumStock;
    }

    /**
     * Gets the minimum stock value.
     *
     * @return the minimum stock value
     */
    public Double getMinimumStock() {
        return minimumStock;
    }

    /**
     * Gets the unit of measurement for the minimum stock.
     *
     * @return the unit of measurement for the minimum stock
     */
    public String getUnitMeasurement() {
        return unitMeasurement;
    }
}