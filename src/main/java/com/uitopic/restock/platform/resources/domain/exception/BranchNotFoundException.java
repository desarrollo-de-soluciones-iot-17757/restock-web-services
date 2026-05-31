package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when a branch is not found within the resources bounded context.
 */
public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String message) {
        super(message);
    }
}
