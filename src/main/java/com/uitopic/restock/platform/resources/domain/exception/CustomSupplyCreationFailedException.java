package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when custom supply creation fails within the resources bounded context.
 */
public class CustomSupplyCreationFailedException extends RuntimeException {
    public CustomSupplyCreationFailedException(String message) {
        super(message);
    }
}
