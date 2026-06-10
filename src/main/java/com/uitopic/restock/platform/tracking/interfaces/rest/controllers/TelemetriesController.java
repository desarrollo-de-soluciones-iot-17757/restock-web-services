package com.uitopic.restock.platform.tracking.interfaces.rest.controllers;

import com.uitopic.restock.platform.tracking.domain.services.TelemetryReadingCommandService;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.TelemetryReadingResource;
import com.uitopic.restock.platform.tracking.interfaces.rest.transform.ReceiveTelemetryReadingCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for handling telemetry data received from edge services. This controller provides endpoints for receiving telemetry readings and device health status updates. The telemetry data, especially the physical stock, is used for comparing with system data and evaluating difference thresholds.
 *
 * <p>
 *     The exposed endpoints include:
 *     <li> POST /api/v1/telemetries </li>
 *     <li> POST /api/v1/devices-health </li>
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Telemetry Receiver", description = "Endpoints for receiving telemetry data from edge services.")
@RequiredArgsConstructor
public class TelemetriesController {

    // Service for handling telemetry reading commands
    private final TelemetryReadingCommandService telemetryReadingCommandService;

    /**
     * Endpoint for receiving telemetry readings from edge services. The telemetry data, specially the physical stock, is used for compare with system data and evaluate difference thresholds.
     *
     * @param resource the telemetry reading resource containing the data sent by the edge service
     */
    @PostMapping("/telemetries")
    @Operation(
            summary = "Receive telemetry reading",
            description = "Endpoint for receiving telemetry readings from edge services. The telemetry data, specially the physical stock, is used for compare with system data and evaluate difference thresholds."
    )
    public void receiveTelemetry(
            @RequestBody TelemetryReadingResource resource
    ) {
        log.info("POST /api/v1/telemetries - Received telemetry reading: {}", resource);

        try {
            var receiveTelemetryCommand = ReceiveTelemetryReadingCommandFromResourceAssembler.toCommandFromResource(resource);
            telemetryReadingCommandService.handle(receiveTelemetryCommand);
        } catch (Exception e) {
            log.error("Error processing telemetry reading: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Endpoint for receiving device health status from edge services. This can be used for monitoring the health of the devices sending telemetry data.
     */
    public void receiveDeviceHealthStatus() {
        log.info("POST /api/v1/devices-health - Received device health status");
    }
}
