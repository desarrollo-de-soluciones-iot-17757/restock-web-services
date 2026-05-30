package com.uitopic.restock.platform.resources.domain.model.valueobjects;

import com.uitopic.restock.platform.resources.domain.exception.InvalidStockQuantityException;

/**
 * Value object representing the stock quantity of a product in the inventory. It encapsulates the logic for validating and manipulating stock quantities, ensuring that they are always non-negative and providing methods for adding and subtracting stock.
 *
 * @param stock the quantity of stock, which must be a non-negative integer
 */
public record Stock(Integer stock) {

    // Constructor to validate the stock quantity
    public Stock {
        if (stock < 0) {
            throw new InvalidStockQuantityException("Stock quantity cannot be negative");
        }
    }

    /**
     * Add the given stock quantity to the current stock and return a new Stock instance with the updated quantity.
     *
     * @param other the stock quantity to add to the current stock
     * @return a new Stock instance with the resulting stock quantity after addition
     */
    public Stock add(Stock other) {
        return new Stock(this.stock + other.stock);
    }

    /**
     * Subtract the given stock quantity from the current stock. If the resulting stock quantity is negative, an exception is thrown.
     *
     * @param other the stock quantity to subtract from the current stock
     * @return a new Stock instance with the resulting stock quantity after subtraction
     * @throws InvalidStockQuantityException if the resulting stock quantity is negative
     */
    public Stock subtrack(Stock other) {
        if (other.stock > this.stock) {
            throw new InvalidStockQuantityException("Cannot subtract more stock than available");
        }
        return new Stock(this.stock - other.stock);
    }

    /**
     * Get the current stock quantity.
     * @return the current stock quantity as an Integer.
     */
    public Integer getValue() {
        return this.stock;
    }
}
