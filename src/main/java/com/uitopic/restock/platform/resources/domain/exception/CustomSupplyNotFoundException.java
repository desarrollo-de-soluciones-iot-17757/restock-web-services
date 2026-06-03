package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Custom exception thrown when a supply is not found in the system.
 */
public class CustomSupplyNotFoundException extends RuntimeException {
    public CustomSupplyNotFoundException(String message) {
        super(message);
    }
}
