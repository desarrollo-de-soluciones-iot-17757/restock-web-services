package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when the format of the minimum stock value is invalid.
 * This can occur when the minimum stock value is not a non-negative integer or when the unit of measurement is missing or invalid.
 */
public class InvalidMinimumStockFormatException extends RuntimeException {
    public InvalidMinimumStockFormatException(String message) {
        super(message);
    }
}
