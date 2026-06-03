package com.uitopic.restock.platform.planning.domain.exception;

/**
 * Exception thrown when attempting to add a product that already exists in the system.
 */
public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String message) {
        super(message);
    }
}
