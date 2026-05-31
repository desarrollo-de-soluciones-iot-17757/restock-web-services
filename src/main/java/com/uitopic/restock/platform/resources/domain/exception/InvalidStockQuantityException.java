package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when an invalid stock quantity is provided, such as a negative value.
 */
public class InvalidStockQuantityException extends RuntimeException {
    public InvalidStockQuantityException(String message) {
        super(message);
    }
}
