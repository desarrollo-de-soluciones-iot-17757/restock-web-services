package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationTaskStatus;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.ConciliationTaskPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for conciliation task persistence entities.
 */
@Repository
public interface ConciliationTaskPersistenceRepository extends MongoRepository<ConciliationTaskPersistenceEntity, String> {
    /**
     * Finds all conciliation tasks owned by an account.
     *
     * @param accountId account identifier
     * @return persistence entities for the account
     */
    List<ConciliationTaskPersistenceEntity> findAllByAccountId(AccountId accountId);

    /**
     * Finds conciliation tasks that match the full stock comparison scope and
     * status.
     *
     * @param accountId account identifier
     * @param deviceId device identifier
     * @param customSupplyId custom supply identifier
     * @param batchId batch identifier
     * @param status conciliation task status
     * @return persistence entities matching the scope
     */
    List<ConciliationTaskPersistenceEntity> findAllByAccountIdAndDeviceIdAndCustomSupplyIdAndBatchIdAndConciliationStatus(
            AccountId accountId,
            DeviceId deviceId,
            String customSupplyId,
            BatchId batchId,
            ConciliationTaskStatus status
    );
}
