package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllCustomSuppliesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSuppliesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSupplyByIdQuery;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Application service for CustomSupply read operations.
 */
@Slf4j
@Service
public class CustomSupplyQueryServiceImpl implements CustomSupplyQueryService {

    // Using the domain repository directly for queries, as it's optimized for read operations.
    private final CustomSupplyRepository customSupplyRepository;

    // Constructor injection of the repository
    public CustomSupplyQueryServiceImpl(CustomSupplyRepository customSupplyRepository) {
        this.customSupplyRepository = customSupplyRepository;
    }

    /**
     * Handles the GetAllCustomSuppliesQuery to retrieve all custom supplies.
     *
     * @param query query to get all custom supplies
     * @return list of all custom supplies
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomSupply> handle(GetAllCustomSuppliesQuery query) {
        log.debug("Querying all custom supplies");
        var results = customSupplyRepository.findAll();
        log.debug("Found {} custom supplies", results.size());
        return results;
    }

    /**
     * Handles the GetCustomSuppliesByAccountIdQuery to retrieve custom supplies for a specific account.
     *
     * @param query query with the account identifier
     * @return list of custom supplies associated with the specified account
     */
    @Override
    @Transactional(readOnly = true)
    public List<CustomSupply> handle(GetCustomSuppliesByAccountIdQuery query) {
        log.debug("Querying custom supplies by accountId='{}'", query.accountId());
        var results = customSupplyRepository.findByAccountId(query.accountId());
        log.debug("Found {} custom supplies for accountId='{}'", results.size(), query.accountId());
        return results;
    }

    /**
     * Handles the GetCustomSupplyByIdQuery to retrieve a specific custom supply by its identifier.
     *
     * @param query query with the custom supply identifier
     * @return optional containing the custom supply if found, or empty if not found
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CustomSupply> handle(GetCustomSupplyByIdQuery query) {
        log.debug("Querying custom supply by id='{}'", query.customSupplyId());
        return customSupplyRepository.findById(query.customSupplyId());
    }
}