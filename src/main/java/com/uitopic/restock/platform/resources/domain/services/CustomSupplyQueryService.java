package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSuppliesByAccountIdQuery;

import java.util.List;

/**
 * Service interface for handling queries related to CustomSupply aggregates.
 * This service is responsible for processing queries and returning the appropriate data.
 */
public interface CustomSupplyQueryService {

    /**
     * Handles the GetCustomSuppliesByAccountIdQuery and returns a list of CustomSupply aggregates.
     *
     * @param query the query object containing the account ID for which to fetch the custom supplies
     * @return a list of CustomSupply aggregates that are associated with the specified account ID
     */
    List<CustomSupply> handle(GetCustomSuppliesByAccountIdQuery query);
}
