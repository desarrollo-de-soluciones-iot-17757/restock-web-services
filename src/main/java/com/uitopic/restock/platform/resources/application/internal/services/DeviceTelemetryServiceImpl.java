package com.uitopic.restock.platform.resources.application.internal.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeviceTelemetryServiceImpl implements DeviceTelemetryService {

    @Override
    public void disableTelemetryForBranch(String branchId) {
        // currently we only log the action. Integration with devices/tracking should be added here.
        log.info("Disabling telemetry for devices attached to branchId={}", branchId);
    }
}

