package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities.BatchPersistenceEntity;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository for Batch documents.
 */
@Repository
public interface BatchPersistenceRepository extends MongoRepository<BatchPersistenceEntity, String> {

    /**
     * Finds all batches that belong to an account.
     *
     * @param accountId account identifier
     * @return batches owned by the account
     */
    List<BatchPersistenceEntity> findByAccountId(AccountId accountId);

    /**
     * Finds all batches stored in a branch.
     *
     * @param branchId branch identifier
     * @return batches stored in the branch
     */
    List<BatchPersistenceEntity> findByBranchId(String branchId);

    /**
     * Finds all batches associated with a custom supply.
     *
     * @param customSupplyId custom supply identifier
     * @return batches associated with the custom supply
     */
    List<BatchPersistenceEntity> findByCustomSupplyId(String customSupplyId);

    /**
     * Finds the first batch of a custom supply stored in a branch.
     *
     * @param branchId branch identifier
     * @param customSupplyId custom supply identifier
     * @return compatible batch if found
     */
    Optional<BatchPersistenceEntity> findFirstByBranchIdAndCustomSupplyId(String branchId, String customSupplyId);

    /**
     * Finds the first compatible batch by code, custom supply, branch and expiration date.
     *
     * @param code batch code
     * @param customSupplyId custom supply identifier
     * @param branchId branch identifier
     * @param expirationDate expiration date
     * @return compatible batch if found
     */
    Optional<BatchPersistenceEntity> findFirstByCodeAndCustomSupplyIdAndBranchIdAndExpirationDate(
            String code,
            String customSupplyId,
            String branchId,
            LocalDate expirationDate
    );

}