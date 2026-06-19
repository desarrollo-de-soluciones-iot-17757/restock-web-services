package com.uitopic.restock.platform.sales.domain.exceptions;

/**
 * Base exception for Sales domain errors.
 */
public class SalesDomainException extends RuntimeException {
    public SalesDomainException(String message) {
        super(message);
    }

    public SalesDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

