package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when a batch is not found in the system.
 */
public class BatchNotFoundException extends RuntimeException {
    public BatchNotFoundException(String message) {
        super(message);
    }
}
