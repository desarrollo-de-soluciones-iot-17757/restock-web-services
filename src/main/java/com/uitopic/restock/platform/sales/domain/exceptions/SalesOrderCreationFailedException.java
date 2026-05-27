package com.uitopic.restock.platform.sales.domain.exceptions;

public class SalesOrderCreationFailedException extends RuntimeException {
    public SalesOrderCreationFailedException(String message) {
        super(message);
    }
}
