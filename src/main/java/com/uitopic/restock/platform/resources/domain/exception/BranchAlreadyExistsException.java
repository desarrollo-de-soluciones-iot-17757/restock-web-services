package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when attempting to create a branch that already exists.
 * This is a runtime exception that indicates a conflict in branch creation.
 */
public class BranchAlreadyExistsException extends RuntimeException {
    public BranchAlreadyExistsException(String message) {
        super(message);
    }
}
