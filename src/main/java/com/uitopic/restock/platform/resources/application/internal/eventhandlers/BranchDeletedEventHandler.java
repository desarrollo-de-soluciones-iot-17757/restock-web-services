package com.uitopic.restock.platform.resources.application.internal.eventhandlers;

import com.uitopic.restock.platform.devices.domain.services.DeviceTelemetryService;
import com.uitopic.restock.platform.resources.domain.model.events.BranchDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


/**
 * Handles BranchDeletedEvent after a branch is deactivated.
 */
@Slf4j
@Component
public class BranchDeletedEventHandler {

    private final DeviceTelemetryService deviceTelemetryService;

    public BranchDeletedEventHandler(DeviceTelemetryService deviceTelemetryService) {
        this.deviceTelemetryService = deviceTelemetryService;
    }

    /**
     * Disables telemetry for devices attached to the deactivated branch.
     *
     * @param event branch deleted event
     */
    @EventListener
    public void on(BranchDeletedEvent event) {
        log.info("Branch deactivated: branchId={}, accountId={}", event.branchId(), event.accountId());

        try {
            deviceTelemetryService.disableTelemetryForBranch(event.branchId());
        } catch (Exception ex) {
            log.warn("Failed to disable telemetry for branchId={}: {}", event.branchId(), ex.getMessage());
        }
    }
}