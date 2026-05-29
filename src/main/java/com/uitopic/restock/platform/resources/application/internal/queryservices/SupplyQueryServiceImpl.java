package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.services.SupplyQueryService;
import com.uitopic.restock.platform.resources.infrastructure.repositories.SupplyRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the SupplyQueryService interface that handles queries related to Supply entities.
 * This service is responsible for fetching Supply data from the database and returning it to the caller.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class SupplyQueryServiceImpl implements SupplyQueryService {

    // Repository for accessing Supply data from the database
    private final SupplyRepositoryImpl supplyRepository;

    // Constructor injection of the SupplyRepositoryImpl to allow for database access
    public SupplyQueryServiceImpl(SupplyRepositoryImpl supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    /**
     * Handles the GetAllSuppliesQuery and returns a list of Supply entities.
     *
     * @param query the query object containing any necessary parameters for fetching the supplies
     * @return a list of Supply entities that match the criteria specified in the query
     */
    @Override
    public List<Supply> handle(GetAllSuppliesQuery query) {
        log.debug("Querying all supplies templates");
        var results = supplyRepository.findAll();
        log.debug("Found {} supplies templates", results.size());
        return results;
    }
}
