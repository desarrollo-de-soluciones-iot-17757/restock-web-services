package com.uitopic.restock.platform.tracking.interfaces.rest.transform;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.PhysicalAnomaly;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.PhysicalAnomalyResource;

/**
 * Assembler to transform PhysicalAnomaly domain entity to PhysicalAnomalyResource response.
 */
public class PhysicalAnomalyResourceFromEntityAssembler {
    public static PhysicalAnomalyResource toResourceFromEntity(PhysicalAnomaly entity) {
        return new PhysicalAnomalyResource(
                entity.getId(),
                entity.getDeviceId() != null ? entity.getDeviceId().getDeviceId() : null,
                entity.getRegisteredValue(),
                entity.getTimestamp()
        );
    }
}
