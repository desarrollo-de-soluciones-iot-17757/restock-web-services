package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.services.BranchQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Query service implementation for {@link Branch} aggregates within the resources bounded context.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class BranchQueryServiceImpl implements BranchQueryService {

    private final BranchRepository branchRepository;

    public BranchQueryServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    /**
     * Finds a single branch by its unique identifier.
     *
     * @param id the unique identifier of the branch
     * @return an {@link Optional} containing the {@link Branch} if found, or empty if not found
     */
    @Override
    public Optional<Branch> findById(String id) {
        return branchRepository.findById(id);
    }

    /**
     * Retrieves all branches associated with the account specified in the query.
     *
     * @param query the query containing the account ID
     * @return a {@link List} of {@link Branch} aggregates for that account
     */
    @Override
    public List<Branch> handle(GetBranchesByAccountIdQuery query) {
        log.debug("Querying branches for account ID: {}", query.accountId());
        var results = branchRepository.findByAccountId(query.accountId());
        log.debug("Found {} branches for account ID: {}", results.size(), query.accountId());
        return results;
    }

    /**
     * Retrieves a paginated and optionally filtered list of branches for the account
     * specified in the query. Filtering and pagination are applied in-memory.
     *
     * @param query the query containing the account ID
     * @param state optional status filter (e.g., {@code "ACTIVE"} or {@code "INACTIVE"}); pass {@code null} or blank to return all
     * @param page  zero-based page index
     * @param size  maximum number of results per page
     * @return a {@link List} of {@link Branch} aggregates matching the filter and page window
     */
    @Override
    public List<Branch> handle(GetBranchesByAccountIdQuery query, String state, int page, int size) {
        log.debug("Querying branches for account ID: {} — state={}, page={}, size={}", query.accountId(), state, page, size);
        var all = branchRepository.findByAccountId(query.accountId());

        var filtered = all.stream()
                .filter(b -> {
                    if (state == null || state.isBlank()) return true;
                    try {
                        var s = com.uitopic.restock.platform.resources.domain.model.valueobjects.BranchStates.valueOf(state.toUpperCase());
                        return b.getStatus() == s;
                    } catch (IllegalArgumentException ex) {
                        return false;
                    }
                }).toList();

        int from = Math.max(0, page) * Math.max(1, size);
        int to = Math.min(filtered.size(), from + Math.max(1, size));
        var result = from >= filtered.size() ? List.<Branch>of() : filtered.subList(from, to);

        log.debug("Returning {} branches after filter/pagination", result.size());
        return result;
    }
}
