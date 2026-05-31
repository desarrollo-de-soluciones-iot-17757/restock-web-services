package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSuppliesQuery;

import java.util.List;
import java.util.Optional;

/**
 * Domain service interface defining the query contract for
 * {@link com.uitopic.restock.platform.resources.domain.model.entities.Supply}
 * retrieval within the resources bounded context.
 */
public interface SupplyQueryService {
    List<Supply> handle(GetAllSuppliesQuery query);
    Optional<Supply> findById(String id);
}
