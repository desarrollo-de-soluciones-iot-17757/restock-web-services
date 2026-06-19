package com.uitopic.restock.platform.tracking.infrastructure.adapters;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.model.aggregates.ConciliationTask;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationTaskStatus;
import com.uitopic.restock.platform.tracking.domain.repositories.ConciliationTaskRepository;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.assemblers.ConciliationTaskPersistenceAssembler;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories.ConciliationTaskPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB-backed implementation of the ConciliationTaskRepository contract.
 *
 * <p>
 * Converts between conciliation task aggregates and persistence entities using
 * the corresponding assembler, and provides the query operations needed by the
 * tracking application services.
 */
@Repository
@RequiredArgsConstructor
public class ConciliationTaskRepositoryImpl implements ConciliationTaskRepository {
    private final ConciliationTaskPersistenceRepository conciliationTaskMongoRepository;

    /**
     * @inheritDocs
     */
    @Override
    public ConciliationTask save(ConciliationTask conciliationTask) {
        var saved = conciliationTaskMongoRepository.save(
                ConciliationTaskPersistenceAssembler.toPersistenceFromDomain(conciliationTask));
        return ConciliationTaskPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * @inheritDocs
     */
    @Override
    public Optional<ConciliationTask> findById(String id) {
        return conciliationTaskMongoRepository.findById(id)
                .map(ConciliationTaskPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * @inheritDocs
     */
    @Override
    public List<ConciliationTask> findAllByAccountId(AccountId accountId) {
        return conciliationTaskMongoRepository.findAllByAccountId(accountId).stream()
                .map(ConciliationTaskPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    /**
     * @inheritDocs
     */
    @Override
    public List<ConciliationTask> findAllByCustomSupplyId(String customSupplyId) {
        return conciliationTaskMongoRepository.findAllByCustomSupplyId(customSupplyId).stream()
                .map(ConciliationTaskPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    /**
     * @inheritDocs
     */
    @Override
    public List<ConciliationTask> findAllByAccountIdAndFilters(AccountId accountId, ConciliationTaskStatus status, String customSupplyId, BranchId branchId, DeviceId deviceId) {
        return findAllByAccountId(accountId).stream()
                .filter(task -> status == null || task.getConciliationStatus() == status)
                .filter(task -> customSupplyId == null || customSupplyId.isBlank() || customSupplyId.equals(task.getCustomSupplyId()))
                .filter(task -> branchId == null || branchId.equals(task.getBranchId()))
                .filter(task -> deviceId == null || deviceId.equals(task.getDeviceId()))
                .toList();
    }

    /**
     * @inheritDocs
     */
    @Override
    public List<ConciliationTask> findPendingByScope(AccountId accountId, DeviceId deviceId, String customSupplyId, BatchId batchId) {
        return conciliationTaskMongoRepository
                .findAllByAccountIdAndDeviceIdAndCustomSupplyIdAndBatchIdAndConciliationStatus(
                        accountId, deviceId, customSupplyId, batchId, ConciliationTaskStatus.PENDING)
                .stream()
                .map(ConciliationTaskPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
