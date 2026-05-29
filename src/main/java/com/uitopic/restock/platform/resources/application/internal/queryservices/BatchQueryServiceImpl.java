package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByBranchIdQuery;
import com.uitopic.restock.platform.resources.domain.services.BatchQueryService;
import com.uitopic.restock.platform.resources.infrastructure.repositories.BatchRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class BatchQueryServiceImpl implements BatchQueryService {

    private final BatchRepositoryImpl batchRepository;

    public BatchQueryServiceImpl(BatchRepositoryImpl batchRepository) {
        this.batchRepository = batchRepository;
    }

    @Override
    public List<Batch> handle(GetBatchesByBranchIdQuery query) {
        log.debug("Querying batches for branchId={}, customSupplyId={}", query.branchId(), query.customSupplyId());
        List<Batch> results;
        if (query.branchId() != null && query.customSupplyId() != null) {
            results = batchRepository.findByBranchIdAndCustomSupplyId(query.branchId(), query.customSupplyId());
        } else if (query.branchId() != null) {
            results = batchRepository.findByBranchId(query.branchId());
        } else if (query.customSupplyId() != null) {
            results = batchRepository.findByCustomSupplyId(query.customSupplyId());
        } else {
            results = batchRepository.findAll();
        }
        log.debug("Found {} batches", results.size());
        return results;
    }

    @Override
    public Optional<Batch> findById(String id) {
        return batchRepository.findById(id);
    }
}
