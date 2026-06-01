package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when an invalid stock quantity is encountered. This can occur when the stock quantity is negative or when there is an attempt to subtract more stock than is available in the inventory.
 */
public class InvalidStockException extends RuntimeException {
    public InvalidStockException(String message) {
        super(message);
    }
}
