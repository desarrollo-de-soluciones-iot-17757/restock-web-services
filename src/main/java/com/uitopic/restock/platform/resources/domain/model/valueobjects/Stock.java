package com.uitopic.restock.platform.resources.domain.model.valueobjects;

/**
 * Value object representing a stock quantity in the resources bounded context.
 *
 * <p>Stored as a plain integer primitive in MongoDB via the registered
 * {@link com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.StockWriteConverter}
 * and {@link com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.converters.StockReadConverter}.
 * This avoids embedding a nested document for a single numeric field.
 *
 * @param stock the stock quantity, must be non-negative
 */
public record Stock(int stock) {

    /**
     * Compact constructor that validates the stock quantity.
     *
     * @throws IllegalArgumentException if {@code stock} is negative
     */
    public Stock {
        if (stock < 0) throw new IllegalArgumentException("Stock quantity cannot be negative: " + stock);
    }

    /**
     * Returns a new {@link Stock} with the given quantity added to the current value.
     *
     * @param quantity the amount to add, must be non-negative
     * @return a new {@link Stock} with the updated quantity
     * @throws IllegalArgumentException if {@code quantity} is negative
     */
    public Stock addStock(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity to add cannot be negative");
        return new Stock(this.stock + quantity);
    }

    /**
     * Returns a new {@link Stock} with the given quantity subtracted from the current value.
     *
     * @param quantity the amount to subtract, must be non-negative and not exceed current stock
     * @return a new {@link Stock} with the updated quantity
     * @throws IllegalArgumentException if {@code quantity} is negative or exceeds current stock
     */
    public Stock subtractStock(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity to subtract cannot be negative");
        if (quantity > this.stock) throw new IllegalArgumentException("Cannot subtract more than current stock");
        return new Stock(this.stock - quantity);
    }

    /**
     * Returns the raw stock quantity value.
     *
     * @return the stock quantity as a primitive int
     */
    public int getStock() {
        return stock;
    }
}
