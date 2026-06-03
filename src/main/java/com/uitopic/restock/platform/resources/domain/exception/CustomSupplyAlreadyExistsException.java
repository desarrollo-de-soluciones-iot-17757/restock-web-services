package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when attempting to create a supply that already exists.
 */
public class CustomSupplyAlreadyExistsException extends RuntimeException {
    public CustomSupplyAlreadyExistsException(String message) {
        super(message);
    }
}
