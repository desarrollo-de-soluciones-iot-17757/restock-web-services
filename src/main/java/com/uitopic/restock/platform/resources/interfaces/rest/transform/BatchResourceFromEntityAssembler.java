package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BatchResource;

public class BatchResourceFromEntityAssembler {

    public static BatchResource toResourceFromEntity(Batch entity) {
        return new BatchResource(
                entity.getId(),
                entity.getAccountId() != null ? entity.getAccountId().getAccountId() : null,
                entity.getBranchId(),
                entity.getCustomSupplyId(),
                entity.getCurrentQuantity(),
                entity.getUnit() != null ? entity.getUnit().getUnitName() : null,
                entity.getExpirationDate(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }
}
