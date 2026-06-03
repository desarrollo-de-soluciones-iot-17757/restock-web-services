package com.uitopic.restock.platform.planning.domain.model.queries;

/**
 * Query to retrieve a single {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}
 * by its unique identifier.
 *
 * @param productId the product's unique identifier
 */
public record GetProductByIdQuery(String productId) {}
