package com.uitopic.restock.platform.planning.domain.exception;

/**
 * Exception thrown when there is a conflict while adding an ingredient to a recipe, such as when the ingredient already exists in the recipe or when there are incompatible ingredients.
 */
public class IngredientAdditionConflictException extends RuntimeException {
    public IngredientAdditionConflictException(String message) {
        super(message);
    }
}
