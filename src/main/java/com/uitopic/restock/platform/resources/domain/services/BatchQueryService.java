package com.uitopic.restock.platform.resources.domain.services;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByBranchIdQuery;

import java.util.List;
import java.util.Optional;

public interface BatchQueryService {
    List<Batch> handle(GetBatchesByBranchIdQuery query);
    Optional<Batch> findById(String id);
}
