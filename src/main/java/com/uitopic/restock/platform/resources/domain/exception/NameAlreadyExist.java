package com.uitopic.restock.platform.resources.domain.exception;

/**
 * Exception thrown when attempting to create a resource with a name that already exists
 * within the resources bounded context.
 */
public class NameAlreadyExist extends RuntimeException {

    /** Constructs a new NameAlreadyExist exception with the specified detail message. The message should provide information about the name that already exists and the context in which the error occurred. */
    public NameAlreadyExist(String message) {
        super(message);
    }
}
