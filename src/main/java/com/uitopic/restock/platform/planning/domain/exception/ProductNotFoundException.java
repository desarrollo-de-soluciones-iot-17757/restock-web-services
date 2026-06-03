package com.uitopic.restock.platform.planning.domain.exception;

/**
 * Exception thrown when a requested
 * {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}
 * does not exist within the {@code planning} bounded context.
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code ProductNotFoundException} with the given product ID embedded
     * in a descriptive message.
     *
     * @param productId the ID that was not found
     */
    public ProductNotFoundException(String productId) {
        super("Product not found: " + productId);
    }
}
