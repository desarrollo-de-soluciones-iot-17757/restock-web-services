package com.uitopic.restock.platform.planning.domain.exceptions;

public class RecipeCreationFailedException extends RuntimeException {
    public RecipeCreationFailedException(String message) {
        super(message);
    }
}
