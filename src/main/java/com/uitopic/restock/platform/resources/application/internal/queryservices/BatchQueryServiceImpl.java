package com.uitopic.restock.platform.resources.application.internal.queryservices;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.queries.GetAllBatchesQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchByIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByAccountIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByBranchIdQuery;
import com.uitopic.restock.platform.resources.domain.model.queries.GetBatchesByCustomSupplyIdQuery;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.services.BatchQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Application service for Batch read operations.
 */
@Slf4j
@Service
public class BatchQueryServiceImpl implements BatchQueryService {

    // Repository for accessing batch data
    private final BatchRepository batchRepository;

    // Constructor with dependency injection of the batch repository
    public BatchQueryServiceImpl(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    /**
     * Retrieves all batches.
     *
     * @param query query to get all batches
     * @return list of batches
     */
    @Override
    public List<Batch> handle(GetAllBatchesQuery query) {
        log.debug("Querying all batches");
        var results = batchRepository.findAll();
        log.debug("Found {} batches", results.size());
        return results;
    }

    /**
     * Retrieves a batch by its identifier.
     *
     * @param query query with the batch identifier
     * @return batch if found
     */
    @Override
    public Optional<Batch> handle(GetBatchByIdQuery query) {
        log.debug("Querying batch by id='{}'", query.batchId());
        return batchRepository.findById(query.batchId());
    }

    /**
     * Retrieves all batches from an account.
     *
     * @param query query with the account identifier
     * @return list of batches
     */
    @Override
    public List<Batch> handle(GetBatchesByAccountIdQuery query) {
        log.debug("Querying batches by accountId='{}'", query.accountId());
        var results = batchRepository.findByAccountId(query.accountId());
        log.debug("Found {} batches for accountId='{}'", results.size(), query.accountId());
        return results;
    }

    /**
     * Retrieves all batches from a branch.
     *
     * @param query query with the branch identifier
     * @return list of batches
     */
    @Override
    public List<Batch> handle(GetBatchesByBranchIdQuery query) {
        log.debug("Querying batches by branchId='{}'", query.branchId());
        var results = batchRepository.findByBranchId(query.branchId());
        log.debug("Found {} batches for branchId='{}'", results.size(), query.branchId());
        return results;
    }

    /**
     * Retrieves all batches from a custom supply.
     *
     * @param query query with the custom supply identifier
     * @return list of batches
     */
    @Override
    public List<Batch> handle(GetBatchesByCustomSupplyIdQuery query) {
        log.debug("Querying batches by customSupplyId='{}'", query.customSupplyId());
        var results = batchRepository.findByCustomSupplyId(query.customSupplyId());
        log.debug("Found {} batches for customSupplyId='{}'", results.size(), query.customSupplyId());
        return results;
    }

    @Override
    public List<Batch> findAllByFilters(String accountId, String branchId, String customSupplyId) {
        var batches = batchRepository.findAll();

        return batches.stream()
                .filter(batch -> accountId == null || accountId.isBlank()
                        || batch.getAccountId().getAccountId().equals(accountId))
                .filter(batch -> branchId == null || branchId.isBlank()
                        || batch.getBranchId().equals(branchId))
                .filter(batch -> customSupplyId == null || customSupplyId.isBlank()
                        || batch.getCustomSupplyId().equals(customSupplyId))
                .toList();
    }
}