package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when a transfer action fails to execute successfully.
 */
public class TransferActionFailedException extends RuntimeException {
    public TransferActionFailedException(String message) {
        super(message);
    }
}
