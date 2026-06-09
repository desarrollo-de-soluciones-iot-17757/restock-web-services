package com.uitopic.restock.platform.planning.domain.exception;

/**
 * Exception thrown when an ingredient is not found in the system.
 */
public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(String message) {
        super(message);
    }
}
