package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.BatchMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of the BatchRepository domain port.
 */
@Repository
public class BatchRepositoryImpl implements BatchRepository {

    /**
     * Spring Data MongoDB repository used to access batch documents.
     */
    private final BatchMongoRepository batchMongoRepository;

    /**
     * Creates a BatchRepositoryImpl with the required MongoDB repository.
     *
     * @param batchMongoRepository MongoDB repository for batches
     */
    public BatchRepositoryImpl(BatchMongoRepository batchMongoRepository) {
        this.batchMongoRepository = batchMongoRepository;
    }

    /**
     * Saves a batch.
     *
     * @param batch batch to save
     * @return saved batch
     */
    @Override
    public Batch save(Batch batch) {
        return batchMongoRepository.save(batch);
    }

    /**
     * Finds all batches.
     *
     * @return list of batches
     */
    @Override
    public List<Batch> findAll() {
        return batchMongoRepository.findAll();
    }

    /**
     * Finds a batch by its identifier.
     *
     * @param id batch identifier
     * @return batch if found
     */
    @Override
    public Optional<Batch> findById(String id) {
        return batchMongoRepository.findById(id);
    }

    /**
     * Finds all batches that belong to an account.
     *
     * @param accountId account identifier
     * @return batches owned by the account
     */
    @Override
    public List<Batch> findByAccountId(AccountId accountId) {
        return batchMongoRepository.findByAccountId(accountId);
    }

    /**
     * Finds all batches stored in a branch.
     *
     * @param branchId branch identifier
     * @return batches stored in the branch
     */
    @Override
    public List<Batch> findByBranchId(String branchId) {
        return batchMongoRepository.findByBranchId(branchId);
    }

    /**
     * Finds all batches associated with a custom supply.
     *
     * @param customSupplyId custom supply identifier
     * @return batches associated with the custom supply
     */
    @Override
    public List<Batch> findByCustomSupplyId(String customSupplyId) {
        return batchMongoRepository.findByCustomSupplyId(customSupplyId);
    }

    /**
     * Deletes a batch by its identifier.
     *
     * @param id batch identifier
     */
    @Override
    public void deleteById(String id) {
        batchMongoRepository.deleteById(id);
    }

    /**
     * Finds the first batch of a custom supply stored in a branch.
     *
     * @param branchId branch identifier
     * @param customSupplyId custom supply identifier
     * @return compatible batch if found
     */
    @Override
    public Optional<Batch> findFirstByBranchIdAndCustomSupplyId(String branchId, String customSupplyId) {
        return batchMongoRepository.findFirstByBranchIdAndCustomSupplyId(branchId, customSupplyId);
    }

    /**
     * Finds the first compatible batch by code, custom supply, branch and expiration date.
     *
     * @param code batch code
     * @param customSupplyId custom supply identifier
     * @param branchId branch identifier
     * @param expirationDate expiration date
     * @return compatible batch if found
     */
    @Override
    public Optional<Batch> findFirstByCodeAndCustomSupplyIdAndBranchIdAndExpirationDate(
            String code,
            String customSupplyId,
            String branchId,
            LocalDate expirationDate
    ) {
        return batchMongoRepository.findFirstByCodeAndCustomSupplyIdAndBranchIdAndExpirationDate(
                code,
                customSupplyId,
                branchId,
                expirationDate
        );
    }
}