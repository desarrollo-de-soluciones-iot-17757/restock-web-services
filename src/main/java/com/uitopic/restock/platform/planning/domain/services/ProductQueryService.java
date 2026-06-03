package com.uitopic.restock.platform.planning.domain.services;

import com.uitopic.restock.platform.planning.domain.model.aggregates.Product;
import com.uitopic.restock.platform.planning.domain.model.queries.GetProductByIdQuery;
import com.uitopic.restock.platform.planning.domain.model.queries.GetProductsByAccountIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Domain service interface defining the query contract for
 * {@link Product} aggregate retrieval within the {@code planning} bounded context.
 *
 * <p>Declares read-side operations. Implementations live in the application layer
 * ({@link com.uitopic.restock.platform.planning.application.internal.queryservices.ProductQueryServiceImpl}).</p>
 */
public interface ProductQueryService {

    /**
     * Retrieves a single product by its unique identifier.
     *
     * @param query the query containing the product ID
     * @return an {@link Optional} containing the {@link Product} if found, or empty otherwise
     */
    Optional<Product> handle(GetProductByIdQuery query);

    /**
     * Retrieves all products belonging to the account specified in the query.
     *
     * @param query the query containing the account ID
     * @return a {@link List} of {@link Product} aggregates for that account
     */
    List<Product> handle(GetProductsByAccountIdQuery query);
}
