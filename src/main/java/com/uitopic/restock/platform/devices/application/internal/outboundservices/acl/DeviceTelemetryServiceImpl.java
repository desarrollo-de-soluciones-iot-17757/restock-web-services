package com.uitopic.restock.platform.devices.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.devices.domain.services.DeviceTelemetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link DeviceTelemetryService} within the resources bounded context.
 */
@Slf4j
@Service
public class DeviceTelemetryServiceImpl implements DeviceTelemetryService {

    @Override
    public void disableTelemetryForBranch(String branchId) {
        // currently we only log the action. Integration with devices/tracking should be added here.
        log.info("Disabling telemetry for devices attached to branchId={}", branchId);
    }
}