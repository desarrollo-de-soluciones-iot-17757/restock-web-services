package com.uitopic.restock.platform.tracking.domain.exceptions;

/**
 * Custom exception thrown when there is an error during the resolution of an inventory discrepancy.
 */
public class DiscrepancyResolutionException extends RuntimeException {
    public DiscrepancyResolutionException(String message) {
        super(message);
    }
}
