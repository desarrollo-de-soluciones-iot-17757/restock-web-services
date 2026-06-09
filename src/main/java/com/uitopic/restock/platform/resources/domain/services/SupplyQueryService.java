package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSupplyCategoriesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetSupplyByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service contract for Supply read operations.
 */
public interface SupplyQueryService {

    /**
     * Retrieves all supplies.
     *
     * @param query query to get all supplies
     * @return list of supplies
     */
    List<Supply> handle(GetAllSuppliesQuery query);

    /**
     * Retrieves a supply by its identifier.
     *
     * @param query query with the supply identifier
     * @return supply if found
     */
    Optional<Supply> handle(GetSupplyByIdQuery query);

    /**
     * Retrieves all available supply categories.
     *
     * @param query query to get all supply categories
     * @return list of supply categories
     */
    List<String> handle(GetAllSupplyCategoriesQuery query);
}