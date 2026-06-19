package com.uitopic.restock.platform.sales.domain.exceptions;

/**
 * Exception thrown when there is insufficient stock to complete a sale.
 */
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}

