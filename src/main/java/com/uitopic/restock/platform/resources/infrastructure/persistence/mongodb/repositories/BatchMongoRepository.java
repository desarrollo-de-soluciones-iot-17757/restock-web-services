package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository for {@link com.uitopic.restock.platform.resources.domain.model.aggregates.Batch}
 * aggregates within the resources bounded context.
 */
@Repository
public interface BatchMongoRepository extends MongoRepository<Batch, String> {
    List<Batch> findByBranchId(String branchId);
    List<Batch> findByCustomSupplyId(String customSupplyId);
    List<Batch> findByBranchIdAndCustomSupplyId(String branchId, String customSupplyId);
    List<Batch> findByAccountId(AccountId accountId);
}
