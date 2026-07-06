package com.uitopic.restock.platform.tracking.interfaces.rest.controllers;

import com.uitopic.restock.platform.tracking.application.internal.commandservices.PhysicalAnomalyCommandService;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.CreatePhysicalAnomalyResource;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.PhysicalAnomalyResource;
import com.uitopic.restock.platform.tracking.interfaces.rest.transform.CreatePhysicalAnomalyCommandFromResourceAssembler;
import com.uitopic.restock.platform.tracking.interfaces.rest.transform.PhysicalAnomalyResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST Controller for receiving physical anomaly reports from IoT Edge services.
 */
@Slf4j
@RestController
@RequestMapping(value = {"/api/v1/anomalies", "/api/v1/tracking/anomalies"}, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Anomalies Receiver", description = "Endpoints for receiving physical scale anomaly reports from edge services.")
@RequiredArgsConstructor
public class AnomaliesController {

    private final PhysicalAnomalyCommandService physicalAnomalyCommandService;

    /**
     * Endpoint for receiving physical scale anomaly reports from edge services.
     *
     * @param resource the anomaly report payload sent by the edge service.
     * @return 201 Created with the persisted anomaly details.
     */
    @PostMapping
    @Operation(
            summary = "Report physical anomaly",
            description = "Receives a physical scale reading anomaly report from an edge device and stores it."
    )
    public ResponseEntity<PhysicalAnomalyResource> reportAnomaly(
            @Valid @RequestBody CreatePhysicalAnomalyResource resource
    ) {
        log.info("POST /api/v1/anomalies - Received physical anomaly report: {}", resource);

        var command = CreatePhysicalAnomalyCommandFromResourceAssembler.toCommandFromResource(resource);
        var anomaly = physicalAnomalyCommandService.handle(command);
        var responseResource = PhysicalAnomalyResourceFromEntityAssembler.toResourceFromEntity(anomaly);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseResource);
    }
}
