package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Custom exception thrown when a branch is not found in the system.
 */
public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String message) {
        super(message);
    }
}
