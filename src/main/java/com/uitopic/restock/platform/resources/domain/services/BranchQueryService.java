package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBranchesByAccountIdQuery;

import java.util.List;
import java.util.Optional;

public interface BranchQueryService {
    Optional<Branch> findById(String id);
    List<Branch> handle(GetBranchesByAccountIdQuery query);
}
