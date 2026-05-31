package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.interfaces.rest.resources.BatchResource;

public class BatchResourceFromEntityAssembler {

    public static BatchResource toResourceFromEntity(Batch entity) {
        return new BatchResource(
                entity.getId(),
                entity.getAccountId() != null ? entity.getAccountId().getAccountId() : null,
                entity.getReceivingBranch() != null ? entity.getReceivingBranch().getId() : null,
                entity.getCustomSupply() != null ? entity.getCustomSupply().getId() : null,
                entity.getCurrentStock() != null ? entity.getCurrentStock().stock() : 0,
                entity.getCustomSupply() != null && entity.getCustomSupply().getUnitMeasurement() != null 
                        ? entity.getCustomSupply().getUnitMeasurement().getUnitName() : null,
                entity.getExpirationDate() != null ? entity.getExpirationDate().toString() : null,
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }
}
