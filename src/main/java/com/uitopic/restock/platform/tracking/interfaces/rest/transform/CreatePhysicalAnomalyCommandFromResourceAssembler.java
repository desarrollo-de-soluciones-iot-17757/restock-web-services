package com.uitopic.restock.platform.tracking.interfaces.rest.transform;

import com.uitopic.restock.platform.tracking.domain.model.commands.CreatePhysicalAnomalyCommand;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.CreatePhysicalAnomalyResource;

/**
 * Assembler to transform CreatePhysicalAnomalyResource to CreatePhysicalAnomalyCommand.
 */
public class CreatePhysicalAnomalyCommandFromResourceAssembler {
    public static CreatePhysicalAnomalyCommand toCommandFromResource(CreatePhysicalAnomalyResource resource) {
        return new CreatePhysicalAnomalyCommand(
                resource.deviceId(),
                resource.registeredValue(),
                resource.timestamp()
        );
    }
}
