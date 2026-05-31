package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.resources.domain.model.events.BranchDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.uitopic.restock.platform.resources.application.internal.services.DeviceTelemetryService;

@Slf4j
@Component
public class BranchDeletedEventHandler {

    private final DeviceTelemetryService deviceTelemetryService;

    public BranchDeletedEventHandler(DeviceTelemetryService deviceTelemetryService) {
        this.deviceTelemetryService = deviceTelemetryService;
    }

    @EventListener
    public void on(BranchDeletedEvent event) {
        log.info("Branch deleted: branchId={}, accountId={}", event.branchId(), event.accountId());
        // try to disable telemetry for devices attached to this branch. Implementation is local to resources BC.
        try {
            deviceTelemetryService.disableTelemetryForBranch(event.branchId());
        } catch (Exception ex) {
            log.warn("Failed to disable telemetry for branchId={}: {}", event.branchId(), ex.getMessage());
        }
    }
}
