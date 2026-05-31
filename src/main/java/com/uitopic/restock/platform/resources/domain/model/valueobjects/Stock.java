package com.uitopic.restock.platform.resources.domain.model.valueobjects;

import com.uitopic.restock.platform.resources.domain.exception.InvalidStockException;
import com.uitopic.restock.platform.resources.domain.exception.InvalidStockQuantityException;

/**
 * Value object representing the stock quantity of a product in the inventory. It encapsulates the logic for validating and manipulating stock quantities, ensuring that they are always non-negative and providing methods for adding and subtracting stock.
 *
 * @param stock the quantity of stock, which must be a non-negative integer
 * @param unitMeasurement the unit of measurement for the stock quantity (e.g., "units", "kg", "liters", etc.)
 */
public record Stock(
        Double stock,
        String unitMeasurement
) {

    // Constructor to validate the stock quantity
    public Stock {
        if (stock < 0) {
            throw new InvalidStockQuantityException("Stock quantity cannot be negative");
        }

        if (unitMeasurement.trim().isBlank()) {
            throw new IllegalArgumentException("Unit measurement cannot be blank");
        }
    }

    /**
     * Add the given stock quantity to the current stock and return a new Stock instance with the updated quantity.
     *
     * @param other the stock quantity to add to the current stock
     * @return a new Stock instance with the resulting stock quantity after addition
     */
    public Stock add(Stock other) {
        if (!other.unitMeasurement().equals(this.unitMeasurement)) {
            throw new InvalidStockException("Unit measurement does not match");
        }

        return new Stock(this.stock + other.stock(), this.unitMeasurement);
    }

    /**
     * Subtract the given stock quantity from the current stock. If the resulting stock quantity is negative, an exception is thrown.
     *
     * @param other the stock quantity to subtract from the current stock
     * @return a new Stock instance with the resulting stock quantity after subtraction
     * @throws InvalidStockException if the resulting stock quantity is negative
     */
    public Stock subtrack(Stock other) {
        if (other.stock > this.stock) {
            throw new InvalidStockException("Cannot subtract more stock than available");
        }

        if (!other.unitMeasurement().equals(this.unitMeasurement)) {
            throw new InvalidStockException("Unit measurement does not match");
        }

        return new Stock(this.stock - other.stock(), this.unitMeasurement);
    }

    /**
     * Get the current stock quantity.
     * @return the current stock quantity as an Integer.
     */
    public Double getValue() {
        return this.stock;
    }

    /**
     * Get the unit of measurement for the stock quantity.
     * @return the unit of measurement as a String.
     */
    public String getUnit() {
        return this.unitMeasurement;
    }
}