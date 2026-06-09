package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllCustomSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSuppliesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSupplyByIdQuery;

import java.util.List;
import java.util.Optional;

/**
 * Query service contract for CustomSupply read operations.
 */
public interface CustomSupplyQueryService {

    /**
     * Retrieves all custom supplies.
     *
     * @param query query to get all custom supplies
     * @return list of custom supplies
     */
    List<CustomSupply> handle(GetAllCustomSuppliesQuery query);

    /**
     * Retrieves all custom supplies from an account.
     *
     * @param query query with the account identifier
     * @return list of custom supplies for the account
     */
    List<CustomSupply> handle(GetCustomSuppliesByAccountIdQuery query);

    /**
     * Retrieves a custom supply by its identifier.
     *
     * @param query query with the custom supply identifier
     * @return custom supply if found
     */
    Optional<CustomSupply> handle(GetCustomSupplyByIdQuery query);
}