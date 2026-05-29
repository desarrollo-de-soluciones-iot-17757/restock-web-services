package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSuppliesQuery;

import java.util.List;

/**
 * Service interface for handling queries related to Supply entities.
 * This service is responsible for processing queries and returning the appropriate data.
 */
public interface SupplyQueryService {

    /**
     * Handles the GetAllSuppliesQuery and returns a list of Supply entities.
     *
     * @param query the query object containing any necessary parameters for fetching the supplies
     * @return a list of Supply entities that match the criteria specified in the query
     */
    List<Supply> handle(GetAllSuppliesQuery query);
}
