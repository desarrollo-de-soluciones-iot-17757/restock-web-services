package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.repositories.SupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.SupplyQueryService;
import com.uitopic.restock.platform.resources.infrastructure.repositories.SupplyRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the SupplyQueryService interface, responsible for handling queries related to supply templates. This service interacts with the SupplyRepository to retrieve supply data from the underlying data store, providing methods to fetch all supply templates and find a specific supply template by its unique identifier. The service is designed to be read-only, ensuring that it does not modify any data during query operations, and it includes logging for better traceability and debugging of query executions.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class SupplyQueryServiceImpl implements SupplyQueryService {

    // The SupplyRepository is injected into the service to allow for data retrieval operations related to supply templates. This repository provides methods to access the supply data stored in the underlying data store, enabling the service to perform its query operations effectively.
    private final SupplyRepository supplyRepository;

    // Constructor for the SupplyQueryServiceImpl class, which initializes the supplyRepository field with the provided SupplyRepository instance. This constructor is essential for enabling dependency injection, allowing the service to access the repository methods for retrieving supply data during query operations.
    public SupplyQueryServiceImpl(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    /**
     * Handles the GetAllSuppliesQuery by retrieving all supply templates from the repository. This method logs the query operation and the number of supplies found, providing insights into the query execution process. The method returns a list of Supply entities, which represent the supply templates available in the system.
     *
     * @param query the GetAllSuppliesQuery containing the parameters for retrieving all supply templates. This query does not require any specific parameters, as it is designed to fetch all available supplies.
     * @return a list of Supply entities representing the supply templates found in the repository. If no supplies are found, an empty list is returned. The method ensures that the query execution is logged for debugging purposes, allowing for better traceability and monitoring of the query operations.
     */
    @Override
    public List<Supply> handle(GetAllSuppliesQuery query) {
        log.debug("Querying all supplies templates");
        var results = supplyRepository.findAll();
        log.debug("Found {} supplies templates", results.size());
        return results;
    }

    /**
     * Finds a supply template by its unique identifier. This method is essential for retrieving specific supply details based on the supply ID, allowing for accurate supply management and tracking. The method returns an Optional containing the Supply entity if found, or an empty Optional if no matching record exists. This approach ensures that the method can gracefully handle cases where the requested supply template does not exist in the repository, providing a clear indication of the absence of the record.
     *
     * @param id the unique identifier of the supply template to be retrieved. This ID is used to query the repository for the corresponding Supply entity, allowing for precise retrieval of supply details based on the provided identifier.
     * @return an Optional containing the Supply entity if found, or an empty Optional if no matching record exists. This return type allows for a clear and concise way to handle the presence or absence of the requested supply template, enabling the calling code to easily determine whether the retrieval was successful or if the supply template was not found in the repository.
     */
    @Override
    public Optional<Supply> findById(String id) {
        log.debug("Querying supply template by id={}", id);
        return supplyRepository.findById(id);
    }
}
