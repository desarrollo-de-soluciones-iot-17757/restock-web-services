package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository interface for managing Batch entities, providing methods to query batches based on branch ID, custom supply ID, and account ID.
 */
@Repository
public interface BatchMongoRepository extends MongoRepository<Batch, String> {

    /**
     * Finds batches by the custom supply ID, allowing retrieval of all batches associated with a specific supply.
     *
     * @param customSupplyId the custom supply ID for which to find batches
     * @return a list of Batch entities associated with the specified custom supply ID
     */
    List<Batch> findByCustomSupplyId(String customSupplyId);

    /**
     * Finds batches by account ID, allowing retrieval of all batches associated with a specific account, which can be useful for auditing and tracking inventory movements across different accounts.
     *
     * @param accountId the ID of the account for which to find batches
     * @return a list of Batch entities associated with the specified account ID
     */
    List<Batch> findByAccountId(AccountId accountId);
}
