package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when a supply is not found in the system.
 */
public class SupplyNotFoundException extends RuntimeException {
    public SupplyNotFoundException(String message) {
        super(message);
    }
}
