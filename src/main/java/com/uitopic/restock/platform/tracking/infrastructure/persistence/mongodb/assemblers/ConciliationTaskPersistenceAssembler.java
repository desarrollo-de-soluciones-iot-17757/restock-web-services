package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.ConciliationTask;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.ConciliationTaskPersistenceEntity;

/**
 * Assembler class responsible for converting between ConciliationTask domain
 * aggregates and MongoDB persistence entities.
 */
public final class ConciliationTaskPersistenceAssembler {

    private ConciliationTaskPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a persistence entity into a conciliation task domain aggregate.
     *
     * @param entity MongoDB persistence entity
     * @return conciliation task aggregate, or null if the entity is null
     */
    public static ConciliationTask toDomainFromPersistence(ConciliationTaskPersistenceEntity entity) {
        if (entity == null) return null;

        var task = new ConciliationTask();
        task.setId(entity.getId());
        task.setStatus(entity.getStatus());
        task.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toInstant() : null);
        task.setDeviceId(entity.getDeviceId());
        task.setDiscrepancyId(entity.getDiscrepancyId());
        task.setStockComparisonTaskId(entity.getStockComparisonTaskId());
        task.setAccountId(entity.getAccountId());
        task.setBranchId(entity.getBranchId());
        task.setBatchId(entity.getBatchId());
        task.setCustomSupplyId(entity.getCustomSupplyId());
        task.setCustomSupplyName(entity.getCustomSupplyName());
        task.setPhysicalStock(entity.getPhysicalStock());
        task.setSystemStock(entity.getSystemStock());
        task.setJustifiedWithdrawnStockUsed(entity.getJustifiedWithdrawnStockUsed());
        task.setTotalPhysicalStock(entity.getTotalPhysicalStock());
        task.setDifference(entity.getDifference());
        task.setAlertLevel(entity.getAlertLevel());
        task.setConciliationStatus(entity.getConciliationStatus());
        task.setResolutionAction(entity.getResolutionAction());
        task.setResolutionReason(entity.getResolutionReason());
        task.setResolutionJustification(entity.getResolutionJustification());
        task.setResolvedByUserId(entity.getResolvedByUserId());
        task.setResolvedAt(entity.getResolvedAt());
        return task;
    }

    /**
     * Converts a conciliation task domain aggregate into a MongoDB persistence
     * entity.
     *
     * @param domain conciliation task aggregate
     * @return persistence entity ready to be stored, or null if the aggregate
     *         is null
     */
    public static ConciliationTaskPersistenceEntity toPersistenceFromDomain(ConciliationTask domain) {
        if (domain == null) return null;

        var entity = new ConciliationTaskPersistenceEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setStatus(domain.getStatus());
        entity.setDeviceId(domain.getDeviceId());
        entity.setDiscrepancyId(domain.getDiscrepancyId());
        entity.setStockComparisonTaskId(domain.getStockComparisonTaskId());
        entity.setAccountId(domain.getAccountId());
        entity.setBranchId(domain.getBranchId());
        entity.setBatchId(domain.getBatchId());
        entity.setCustomSupplyId(domain.getCustomSupplyId());
        entity.setCustomSupplyName(domain.getCustomSupplyName());
        entity.setPhysicalStock(domain.getPhysicalStock());
        entity.setSystemStock(domain.getSystemStock());
        entity.setJustifiedWithdrawnStockUsed(domain.getJustifiedWithdrawnStockUsed());
        entity.setTotalPhysicalStock(domain.getTotalPhysicalStock());
        entity.setDifference(domain.getDifference());
        entity.setAlertLevel(domain.getAlertLevel());
        entity.setConciliationStatus(domain.getConciliationStatus());
        entity.setResolutionAction(domain.getResolutionAction());
        entity.setResolutionReason(domain.getResolutionReason());
        entity.setResolutionJustification(domain.getResolutionJustification());
        entity.setResolvedByUserId(domain.getResolvedByUserId());
        entity.setResolvedAt(domain.getResolvedAt());
        return entity;
    }
}
