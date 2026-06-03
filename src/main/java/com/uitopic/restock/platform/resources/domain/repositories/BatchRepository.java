package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for Batch aggregate persistence.
 */
public interface BatchRepository {

    /**
     * Saves a batch.
     *
     * @param batch batch to save
     * @return saved batch
     */
    Batch save(Batch batch);

    /**
     * Finds all batches.
     *
     * @return list of batches
     */
    List<Batch> findAll();

    /**
     * Finds a batch by its identifier.
     *
     * @param id batch identifier
     * @return batch if found
     */
    Optional<Batch> findById(String id);

    /**
     * Finds all batches that belong to an account.
     *
     * @param accountId account identifier
     * @return batches owned by the account
     */
    List<Batch> findByAccountId(AccountId accountId);

    /**
     * Finds all batches stored in a branch.
     *
     * @param branchId branch identifier
     * @return batches stored in the branch
     */
    List<Batch> findByBranchId(String branchId);

    /**
     * Finds all batches associated with a custom supply.
     *
     * @param customSupplyId custom supply identifier
     * @return batches associated with the custom supply
     */
    List<Batch> findByCustomSupplyId(String customSupplyId);

    /**
     * Deletes a batch by its identifier.
     *
     * @param id batch identifier
     */
    void deleteById(String id);

    /**
     * Finds the first batch of a custom supply stored in a branch.
     *
     * Used for non-perishable supplies, where stock can be consolidated by
     * custom supply and branch.
     *
     * @param branchId branch identifier
     * @param customSupplyId custom supply identifier
     * @return compatible batch if found
     */
    Optional<Batch> findFirstByBranchIdAndCustomSupplyId(String branchId, String customSupplyId);

    /**
     * Finds the first compatible batch by code, custom supply, branch and expiration date.
     *
     * Used for perishable supplies, where lot and expiration date traceability
     * must be preserved.
     *
     * @param code batch code
     * @param customSupplyId custom supply identifier
     * @param branchId branch identifier
     * @param expirationDate expiration date
     * @return compatible batch if found
     */
    Optional<Batch> findFirstByCodeAndCustomSupplyIdAndBranchIdAndExpirationDate(
            String code,
            String customSupplyId,
            String branchId,
            LocalDate expirationDate
    );
}