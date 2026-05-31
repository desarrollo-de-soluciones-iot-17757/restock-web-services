package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.queries.GetCustomSuppliesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.domain.services.CustomSupplyQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Query service implementation for {@link CustomSupply} aggregates.
 *
 * <p>Handles read-side operations for custom supplies, delegating persistence
 * to the {@link CustomSupplyRepository} port. Marked as read-only transactional to
 * optimize database interactions for query-only workloads.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class CustomSupplyQueryServiceImpl implements CustomSupplyQueryService {

    private final CustomSupplyRepository customSupplyRepository;

    public CustomSupplyQueryServiceImpl(CustomSupplyRepository customSupplyRepository) {
        this.customSupplyRepository = customSupplyRepository;
    }

    /**
     * Retrieves all custom supplies associated with the account specified in the query.
     *
     * @param query the query containing the account ID
     * @return a {@link List} of {@link CustomSupply} aggregates for that account
     */
    @Override
    public List<CustomSupply> handle(GetCustomSuppliesByAccountIdQuery query) {
        log.debug("Querying all custom supplies for account ID: {}", query.accountId());
        var results = customSupplyRepository.findByAccountId(query.accountId());
        log.debug("Found {} custom supplies for account ID: {}", results.size(), query.accountId());
        return results;
    }
}
