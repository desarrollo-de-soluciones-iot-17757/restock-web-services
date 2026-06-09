package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllBranchesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchByIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.domain.services.BranchQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Application service for Branch read operations.
 */
@Slf4j
@Service
public class BranchQueryServiceImpl implements BranchQueryService {

    // Repository for accessing branch data
    private final BranchRepository branchRepository;

    // Constructor injection of the branch repository
    public BranchQueryServiceImpl(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    /**
     * Retrieves all branches.
     *
     * @return list of branches
     */
    @Override
    public List<Branch> handle(GetAllBranchesQuery query) {
        return branchRepository.findAll();
    }

    /**
     * Retrieves a branch by its identifier.
     *
     * @param query query with the branch identifier
     * @return branch if found
     */
    @Override
    public Optional<Branch> handle(GetBranchByIdQuery query) {
        log.debug("Querying branch by id='{}'", query.branchId());
        return branchRepository.findById(query.branchId());
    }

    /**
     * Retrieves all branches from an account.
     *
     * @param query query with the account identifier
     * @return list of branches
     */
    @Override
    public List<Branch> handle(GetBranchesByAccountIdQuery query) {
        log.debug("Querying branches by accountId='{}'", query.accountId());
        var results = branchRepository.findByAccountId(query.accountId());
        log.debug("Found {} branches for accountId='{}'", results.size(), query.accountId());
        return results;
    }
}