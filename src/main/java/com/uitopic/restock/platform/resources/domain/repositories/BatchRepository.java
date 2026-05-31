package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for {@link com.uitopic.restock.platform.resources.domain.model.aggregates.Batch}
 * aggregate persistence within the resources bounded context.
 */
public interface BatchRepository {
    Batch save(Batch batch);
    Optional<Batch> findById(String id);
    List<Batch> findAll();
    List<Batch> findByBranchId(String branchId);
    List<Batch> findByCustomSupplyId(String customSupplyId);
    List<Batch> findByBranchIdAndCustomSupplyId(String branchId, String customSupplyId);
    List<Batch> findByAccountId(AccountId accountId);
    void deleteById(String id);
}
