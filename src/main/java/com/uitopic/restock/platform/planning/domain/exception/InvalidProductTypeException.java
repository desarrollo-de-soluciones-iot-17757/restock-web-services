package com.uitopic.restock.platform.planning.domain.exception;

/**
 * Exception thrown when an invalid product type is encountered during the planning process.
 */
public class InvalidProductTypeException extends RuntimeException {
    public InvalidProductTypeException(String message) {
        super(message);
    }
}
