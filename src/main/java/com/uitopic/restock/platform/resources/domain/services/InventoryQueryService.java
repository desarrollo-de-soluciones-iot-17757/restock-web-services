package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.entities.Inventory;
import com.uitopic.restock.platform.resources.domain.model.queries.GetInventoriesByBranchIdQuery;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

/**
 * Service interface for handling inventory-related queries in the platform.
 * This service is responsible for processing queries related to inventory information, such as retrieving inventories by branch.
 */
public interface InventoryQueryService {

    /**
     * Handles the GetInventoriesByBranchIdQuery and returns a Triple containing Inventory, Batch, and CustomSupply.
     *
     * @param query the query object containing the branch ID for which to retrieve the inventory information
     * @return a Triple containing the Inventory, Batch, and CustomSupply associated with the specified branch ID
     */
    Triple<List<Inventory>, List<Batch>, List<CustomSupply>> handle(GetInventoriesByBranchIdQuery query);
}
