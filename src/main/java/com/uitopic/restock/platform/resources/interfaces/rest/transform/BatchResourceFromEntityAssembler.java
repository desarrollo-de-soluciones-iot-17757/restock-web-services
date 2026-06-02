package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BatchResource;

/**
 * Assembler to convert Batch entities into BatchResource responses.
 */
public class BatchResourceFromEntityAssembler {

    /**
     * Converts a Batch aggregate into a REST resource.
     *
     * @param entity batch aggregate
     * @return batch resource
     */
    public static BatchResource toResourceFromEntity(Batch entity) {
        var stock = entity.getCurrentStock();
        var unitMeasurement = stock != null ? stock.unitMeasurement() : null;

        return new BatchResource(
                entity.getId(),
                entity.getCode(),
                stock != null ? stock.stock() : 0,
                unitMeasurement != null ? unitMeasurement.unitName() : null,
                unitMeasurement != null ? unitMeasurement.abbreviation() : null,
                entity.getCustomSupplyId(),
                entity.getBranchId(),
                entity.getAccountId().getAccountId(),
                entity.getExpirationDate() != null ? entity.getExpirationDate().toString() : null,
                entity.getEntryDate() != null ? entity.getEntryDate().toString() : null,
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }
}