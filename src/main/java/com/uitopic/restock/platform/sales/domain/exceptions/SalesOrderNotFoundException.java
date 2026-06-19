package com.uitopic.restock.platform.sales.domain.exceptions;

/**
 * Exception thrown when a sales order is not found in the system.
 */
public class SalesOrderNotFoundException extends RuntimeException {
    public SalesOrderNotFoundException(String message) {
        super(message);
    }
}