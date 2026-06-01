package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when an inventory item is not found.
 */
public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(String message) {
        super(message);
    }
}
