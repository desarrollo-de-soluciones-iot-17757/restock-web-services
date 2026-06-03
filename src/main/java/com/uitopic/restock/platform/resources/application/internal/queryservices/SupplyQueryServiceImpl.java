package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllSupplyCategoriesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetSupplyByIdQuery;
import com.uitopic.restock.platform.resources.domain.repositories.SupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.SupplyQueryService;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.SupplyPersistenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Application service for Supply read operations.
 */
@Slf4j
@Service
public class SupplyQueryServiceImpl implements SupplyQueryService {

    // Using the persistence repository directly for queries, as it's optimized for read operations.
    private final SupplyRepository supplyRepository;

    // Constructor injection of the persistence repository.
    public SupplyQueryServiceImpl(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    /**
     * Handles the GetAllSuppliesQuery to retrieve a list of all supplies.
     *
     * @param query query to get all supplies
     * @return a list of all supplies in the system
     */
    @Override
    @Transactional(readOnly = true)
    public List<Supply> handle(GetAllSuppliesQuery query) {
        log.debug("Querying all supplies");
        var results = supplyRepository.findAll();
        log.debug("Found {} supplies", results.size());
        return results;
    }

    /**
     * Handles the GetSupplyByIdQuery to retrieve a supply by its unique identifier.
     *
     * @param query query with the supply identifier
     * @return an Optional containing the supply if found, or empty if not found
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Supply> handle(GetSupplyByIdQuery query) {
        log.debug("Querying supply by id='{}'", query.supplyId());
        return supplyRepository.findById(query.supplyId());
    }

    /**
     * Handles the GetAllSupplyCategoriesQuery to retrieve a list of all unique supply categories.
     *
     * @param query query to get all supply categories
     * @return a list of unique supply categories sorted alphabetically
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> handle(GetAllSupplyCategoriesQuery query) {
        log.debug("Querying all supply categories");
        var categories = supplyRepository.findAll()
                .stream()
                .map(Supply::getCategory)
                .filter(category -> category != null && !category.isBlank())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();
        log.debug("Found {} supply categories", categories.size());
        return categories;
    }
}