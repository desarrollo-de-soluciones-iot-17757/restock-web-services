package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.services.BranchQueryService;
import com.uitopic.restock.platform.resources.infrastructure.repositories.BranchRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class BranchQueryServiceImpl implements BranchQueryService {

    private final BranchRepositoryImpl branchRepository;

    public BranchQueryServiceImpl(BranchRepositoryImpl branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public Optional<Branch> findById(String id) {
        return branchRepository.findById(id);
    }

    @Override
    public List<Branch> handle(GetBranchesByAccountIdQuery query) {
        log.debug("Querying branches for account ID: {}", query.accountId());
        var results = branchRepository.findByAccountId(query.accountId());
        log.debug("Found {} branches for account ID: {}", results.size(), query.accountId());
        return results;
    }
}
