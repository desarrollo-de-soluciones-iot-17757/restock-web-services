package com.uitopic.restock.platform.tracking.domain.exceptions;

/**
 * Exception thrown when a stock comparison task is attempted to be completed without all necessary data or conditions being met.
 */
public class StockComparisonIncompletedException extends RuntimeException {
    public StockComparisonIncompletedException(String message) {
        super(message);
    }
}
