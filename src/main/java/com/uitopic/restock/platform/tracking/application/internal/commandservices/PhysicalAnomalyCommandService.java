package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.PhysicalAnomaly;
import com.uitopic.restock.platform.tracking.domain.model.commands.CreatePhysicalAnomalyCommand;

/**
 * Application service interface for processing PhysicalAnomaly commands.
 */
public interface PhysicalAnomalyCommandService {
    PhysicalAnomaly handle(CreatePhysicalAnomalyCommand command);
}
