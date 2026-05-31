package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSuppliesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyQueryService;
import com.uitopic.restock.platform.resources.infrastructure.repositories.CustomSupplyRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the CustomSupplyQueryService interface that handles queries related to CustomSupply aggregates.
 * This service is responsible for fetching CustomSupply data based on specific query criteria, such as account ID.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class CustomSupplyQueryServiceImpl implements CustomSupplyQueryService {

    // Repository for accessing CustomSupply data from the database
    private final CustomSupplyRepository customSupplyRepository;

    // Constructor for dependency injection of the CustomSupplyRepositoryImpl
    public CustomSupplyQueryServiceImpl(CustomSupplyRepositoryImpl customSupplyRepository) {
        this.customSupplyRepository = customSupplyRepository;
    }

    /**
     * Handles the GetCustomSuppliesByAccountIdQuery and returns a list of CustomSupply aggregates.
     *
     * @param query the query object containing the account ID for which to fetch the custom supplies
     * @return a list of CustomSupply aggregates that are associated with the specified account ID
     */
    @Override
    public List<CustomSupply> handle(GetCustomSuppliesByAccountIdQuery query) {
        log.debug("Querying all custom supplies for account ID: {}", query.accountId());
        var results = customSupplyRepository.findByAccountId(query.accountId());
        log.debug("Found {} custom supplies for account ID: {}", results.size(), query.accountId());
        return results;
    }
}