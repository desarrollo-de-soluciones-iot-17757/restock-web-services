package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.entities.Inventory;
import com.uitopic.restock.platform.resources.domain.model.queries.GetInventoriesByBranchIdQuery;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryRepository;
import com.uitopic.restock.platform.resources.domain.services.InventoryQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the InventoryQueryService interface for handling inventory-related queries in the platform.
 * This service is responsible for processing queries related to inventory information, such as retrieving inventories by branch.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class InventoryQueryServiceImpl implements InventoryQueryService {

    // Repositories for accessing inventory, batch, and custom supply data from the database
    private final InventoryRepository inventoryRepository;

    // Repository for accessing custom supply data from the database
    private final CustomSupplyRepository customSupplyRepository;

    // Repository for accessing batch data from the database
    private final BatchRepository batchRepository;

    // Constructor for injecting the required repositories into the service
    public InventoryQueryServiceImpl(InventoryRepository inventoryRepository, CustomSupplyRepository customSupplyRepository, BatchRepository batchRepository) {
        this.inventoryRepository = inventoryRepository;
        this.customSupplyRepository = customSupplyRepository;
        this.batchRepository = batchRepository;
    }

    /**
     * Handles the GetInventoriesByBranchIdQuery and returns a Triple containing Inventory, Batch, and CustomSupply.
     *
     * @param query the query object containing the branch ID for which to retrieve the inventory information
     * @return a Triple containing the Inventory, Batch, and CustomSupply associated with the specified branch ID
     */
    @Override
    public Triple<List<Inventory>, List<Batch>, List<CustomSupply>> handle(GetInventoriesByBranchIdQuery query) {
        List<Inventory> inventories = inventoryRepository.findByBranchId(query.branchId());
        List<Batch> batches = new ArrayList<>(List.of());
        List<CustomSupply> customSupplies = new ArrayList<>(List.of());
        for (var inv : inventories) {
            var batch = batchRepository.findById(inv.getBatchId()).orElse(null);
            batches.add(batch);
            if (batch != null) {
                var customSupply = customSupplyRepository.findById(batch.getCustomSupplyId()).orElse(null);
                customSupplies.add(customSupply);
            }
        }
        log.debug("Retrieved {} inventories for branch ID {}", inventories.size(), query.branchId());
        return Triple.of(inventories, batches, customSupplies);
    }
}
