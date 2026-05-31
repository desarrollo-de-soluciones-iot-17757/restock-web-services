package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSuppliesByAccountIdQuery;

import java.util.List;

/**
 * Domain service interface defining the query contract for {@link CustomSupply} aggregate retrieval.
 *
 * <p>Declares the read-side operations available on custom supplies. Implementations live
 * in the application layer
 * ({@link com.uitopic.restock.platform.resources.application.internal.queryservices.CustomSupplyQueryServiceImpl}).
 */
public interface CustomSupplyQueryService {

    /**
     * Retrieves all custom supplies associated with the account specified in the query.
     *
     * @param query the query containing the account ID
     * @return a {@link List} of {@link CustomSupply} aggregates for that account
     */
    List<CustomSupply> handle(GetCustomSuppliesByAccountIdQuery query);
}
