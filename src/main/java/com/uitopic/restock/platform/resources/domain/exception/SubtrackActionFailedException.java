package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Custom exception thrown when a subtrack action fails due to invalid stock quantity or unit measurement. This exception is used to indicate that an attempt to subtract stock from the inventory has failed, either because the resulting stock quantity would be negative or because the unit of measurement does not match the expected value.
 */
public class SubtrackActionFailedException extends RuntimeException {
    public SubtrackActionFailedException(String message) {
        super(message);
    }
}
