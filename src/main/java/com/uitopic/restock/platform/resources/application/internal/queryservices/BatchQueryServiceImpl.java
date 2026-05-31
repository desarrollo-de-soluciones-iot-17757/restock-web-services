package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByCustomSupplyIdQuery;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.services.BatchQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for handling batch-related queries. This class implements the BatchQueryService interface and provides methods to retrieve batch information based on specific criteria, such as custom supply ID or batch ID. The service interacts with the BatchRepository to access the underlying data store and perform necessary queries to fetch batch details.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class BatchQueryServiceImpl implements BatchQueryService {

    // Repository for managing batch data, providing methods to retrieve batch information based on various criteria. This repository is essential for accessing the underlying data store to perform queries related to batches, such as finding batches by custom supply ID or retrieving specific batch details by ID.
    private final BatchRepository batchRepository;

    // Constructor for BatchQueryServiceImpl, which initializes the batchRepository dependency. This constructor is essential for injecting the BatchRepository into the service, allowing it to perform necessary data retrieval operations related to batches. The use of constructor injection promotes better testability and maintainability of the service by ensuring that dependencies are provided at the time of object creation.
    public BatchQueryServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    /**
     * Handles the query to retrieve batches based on a custom supply ID. This method is responsible for processing the GetBatchesByCustomSupplyIdQuery and returning a list of Batch objects that match the specified criteria. The implementation of this method will involve querying the underlying data source to find batches associated with the given custom supply ID and returning them in a structured format.
     *
     * @param query the query object containing the custom supply ID for which batches need to be retrieved. This object encapsulates the necessary information to perform the query, allowing for a clean separation of concerns and facilitating the retrieval of batch data based on specific criteria.
     * @return a list of Batch objects that match the criteria specified in the query, or an empty list if no matching batches are found. The returned list will contain the relevant batch information, such as batch ID, custom supply ID, quantity, and other pertinent details related to the batches associated with the specified custom supply ID.
     */
    @Override
    public List<Batch> handle(GetBatchesByCustomSupplyIdQuery query) {
        log.debug("Handling query to get batches by custom supply ID: {}", query.customSupplyId());
        var results = batchRepository.findByCustomSupplyId(query.customSupplyId());
        log.debug("Found {} batches for custom supply ID: {}", results.size(), query.customSupplyId());
        return results;
    }

    @Override
    public Optional<Batch> findById(String id) {
        log.debug("Fetching batch with ID: {}", id);
        return batchRepository.findById(id);
    }
}
