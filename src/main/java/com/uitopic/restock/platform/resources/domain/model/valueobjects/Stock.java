package com.uitopic.restock.platform.resources.domain.model.valueobjects;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;

/**
 * Value object representing a stock quantity with its unit of measurement.
 *
 * @param stock quantity value
 * @param unitMeasurement unit used for the quantity
 */
public record Stock(
        Double stock,
        UnitMeasurement unitMeasurement
) {
    public Stock {
        if (stock == null) {
            throw new IllegalArgumentException("Stock cannot be null");
        }

        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        if (unitMeasurement == null) {
            throw new IllegalArgumentException("Unit measurement cannot be null");
        }
    }

    /**
     * Adds stock using the same unit of measurement.
     *
     * @param quantity quantity to add
     * @return resulting stock
     */
    public Stock add(Stock quantity) {
        validateSameUnit(quantity);

        return new Stock(
                this.stock + quantity.stock(),
                this.unitMeasurement
        );
    }

    /**
     * Subtracts stock using the same unit of measurement.
     *
     * @param quantity quantity to subtract
     * @return resulting stock
     */
    public Stock subtract(Stock quantity) {
        validateSameUnit(quantity);

        double result = this.stock - quantity.stock();

        if (result < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        return new Stock(result, this.unitMeasurement);
    }

    private void validateSameUnit(Stock quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }

        if (!this.unitMeasurement.equals(quantity.unitMeasurement())) {
            throw new IllegalArgumentException("Stock unit measurement does not match");
        }
    }

    /**
     * Get the current stock quantity.
     * @return the current stock quantity as an Integer.
     */
    public Double getStock() {
        return this.stock;
    }

    /**
     * Get the unit of measurement for the stock quantity.
     * @return the unit of measurement as a String.
     */
    public String getUnit() {
        return this.unitMeasurement.unitName();
    }
}