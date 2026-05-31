package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.entities.InventoryDeduction;
import com.uitopic.restock.platform.resources.domain.model.queries.GetInventoryDeductionsByBatchId;
import com.uitopic.restock.platform.resources.domain.repositories.InventoryDeductionRepository;
import com.uitopic.restock.platform.resources.domain.services.DeductionQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for handling inventory deduction queries. This class is responsible for processing queries related to inventory deductions, such as retrieving inventory deductions by batch ID and finding specific inventory deductions by their unique identifier. It interacts with the InventoryDeductionRepository to perform the necessary data retrieval operations, ensuring that the inventory deduction information is accurately fetched and returned to the caller.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class InventoryDeductionQueryServiceImpl implements DeductionQueryService {

    // Using the interface type for better abstraction and flexibility
    private final InventoryDeductionRepository repository;

    // Constructor injection of the repository implementation, allowing for better testability and separation of concerns
    public InventoryDeductionQueryServiceImpl(InventoryDeductionRepository repository) {
        this.repository = repository;
    }

    /**
     * Handles the query to retrieve inventory deductions by batch ID. This method logs the query parameters for debugging purposes and then uses the repository to fetch the list of inventory deductions associated with the specified batch ID. The results are returned as a list of InventoryDeduction objects, which may be empty if no matching records are found.
     *
     * @param query the query object containing the batch ID for which inventory deductions are being retrieved
     * @return a list of InventoryDeduction objects associated with the specified batch ID, or an empty list if no records are found
     */
    @Override
    public List<InventoryDeduction> handle(GetInventoryDeductionsByBatchId query) {
        log.debug("Querying inventory deductions for branchId={}", query.branchId());
        return repository.findByBranchId(query.branchId());
    }

    /**
     * Finds an inventory deduction by its unique identifier. This method uses the repository to retrieve the inventory deduction record associated with the specified ID. The result is returned as an Optional containing the InventoryDeduction object if found, or an empty Optional if no matching record exists.
     *
     * @param id the unique identifier of the inventory deduction record to be retrieved
     * @return an Optional containing the InventoryDeduction record if found, or an empty Optional if no matching record exists
     */
    @Override
    public Optional<InventoryDeduction> findById(String id) {
        return repository.findById(id);
    }
}
