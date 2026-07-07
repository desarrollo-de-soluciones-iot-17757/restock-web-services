package com.uitopic.restock.platform.tracking.interfaces.rest.controllers;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.model.queries.GetTelemetryReadingsByDeviceIdQuery;
import com.uitopic.restock.platform.tracking.domain.services.TelemetryReadingCommandService;
import com.uitopic.restock.platform.tracking.domain.services.TelemetryReadingQueryService;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.TelemetryReadingHistoryResource;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.TelemetryReadingResource;
import com.uitopic.restock.platform.tracking.interfaces.rest.transform.ReceiveTelemetryReadingCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for handling telemetry data received from edge services. This controller provides endpoints for receiving telemetry readings and device health status updates. The telemetry data, especially the physical stock, is used for comparing with system data and evaluating difference thresholds.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Telemetry Receiver", description = "Endpoints for receiving telemetry data from edge services.")
@RequiredArgsConstructor
public class TelemetriesController {

    private final TelemetryReadingCommandService telemetryReadingCommandService;
    private final TelemetryReadingQueryService telemetryReadingQueryService;

    /**
     * Endpoint for receiving telemetry readings from edge services. The telemetry data, specially the physical stock, is used for compare with system data and evaluate difference thresholds.
     *
     * @param resource the telemetry reading resource containing the data sent by the edge service
     */
    @PostMapping({"/telemetries", "/tracking/metrics"})
    @Operation(
            summary = "Receive telemetry reading",
            description = "Endpoint for receiving telemetry readings from edge services. The telemetry data, specially the physical stock, is used for compare with system data and evaluate difference thresholds."
    )
    public void receiveTelemetry(
            @RequestBody TelemetryReadingResource resource
    ) {
        log.info("POST /api/v1/telemetries or /api/v1/tracking/metrics - Received telemetry reading: {}", resource);

        try {
            var receiveTelemetryCommand = ReceiveTelemetryReadingCommandFromResourceAssembler.toCommandFromResource(resource);
            telemetryReadingCommandService.handle(receiveTelemetryCommand);
        } catch (Exception e) {
            log.error("Error processing telemetry reading: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Endpoint for retrieving telemetry readings by device ID.
     *
     * @param deviceId the unique identifier of the device
     * @return list of telemetry readings
     */
    @GetMapping("/telemetry-readings")
    @Operation(
            summary = "Get telemetry readings for a device",
            description = "Endpoint for retrieving telemetry readings for a specific device, containing physical stock, temperature, humidity, and timestamps."
    )
    public List<TelemetryReadingHistoryResource> getTelemetryReadings(
            @RequestParam String deviceId
    ) {
        log.info("GET /api/v1/telemetry-readings - Fetching readings for device: {}", deviceId);
        var query = new GetTelemetryReadingsByDeviceIdQuery(new DeviceId(deviceId));
        var readings = telemetryReadingQueryService.handle(query);
        return readings.stream()
                .map(r -> new TelemetryReadingHistoryResource(
                        r.getId(),
                        r.getPhysicalStock() != null ? r.getPhysicalStock().stock() : null,
                        r.getTemperatureInCelsius() != null ? r.getTemperatureInCelsius().temperatureInCelsius() : null,
                        r.getHumidityPercentage() != null ? r.getHumidityPercentage().humidityPercentage() : null,
                        r.getTimestamp(),
                        r.getDeviceId().getDeviceId()
                ))
                .toList();
    }
}
