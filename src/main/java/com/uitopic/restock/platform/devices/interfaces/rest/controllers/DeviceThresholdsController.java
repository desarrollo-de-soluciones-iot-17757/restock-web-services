package com.uitopic.restock.platform.devices.interfaces.rest.controllers;

import com.uitopic.restock.platform.devices.domain.model.commands.CreateDeviceThresholdCommand;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdByIdQuery;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdsByAccountIdQuery;
import com.uitopic.restock.platform.devices.domain.services.DeviceThresholdCommandService;
import com.uitopic.restock.platform.devices.domain.services.DeviceThresholdQueryService;
import com.uitopic.restock.platform.devices.interfaces.rest.resources.CreateDeviceThresholdResource;
import com.uitopic.restock.platform.devices.interfaces.rest.resources.DeviceThresholdResource;
import com.uitopic.restock.platform.devices.interfaces.rest.transform.DeviceThresholdResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/device-thresholds", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Device Thresholds", description = "Supply threshold configuration for IoT devices.")
public class DeviceThresholdsController {

    private final DeviceThresholdCommandService thresholdCommandService;
    private final DeviceThresholdQueryService thresholdQueryService;

    public DeviceThresholdsController(
            DeviceThresholdCommandService thresholdCommandService,
            DeviceThresholdQueryService thresholdQueryService
    ) {
        this.thresholdCommandService = thresholdCommandService;
        this.thresholdQueryService = thresholdQueryService;
    }

    @Operation(summary = "Get all thresholds for an account")
    @GetMapping
    public ResponseEntity<List<DeviceThresholdResource>> getAll(@RequestParam String accountId) {
        var thresholds = thresholdQueryService.handle(new GetDeviceThresholdsByAccountIdQuery(accountId));
        var resources = thresholds.stream()
                .map(DeviceThresholdResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get threshold by ID")
    @GetMapping("/{thresholdId}")
    public ResponseEntity<DeviceThresholdResource> getById(@PathVariable String thresholdId) {
        var threshold = thresholdQueryService.handle(new GetDeviceThresholdByIdQuery(thresholdId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Device threshold not found: " + thresholdId
                ));
        return ResponseEntity.ok(DeviceThresholdResourceFromEntityAssembler.toResourceFromEntity(threshold));
    }

    @Operation(summary = "Create a supply threshold for a device")
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceThresholdResource> create(@Valid @RequestBody CreateDeviceThresholdResource resource) {
        var command = new CreateDeviceThresholdCommand(
                resource.accountId(),
                resource.deviceId(),
                resource.customSupplyId(),
                resource.minStock(),
                resource.maxStock(),
                resource.anomalyThreshold(),
                resource.minTemperatureCelsius(),
                resource.maxTemperatureCelsius(),
                resource.minHumidityPercentage(),
                resource.maxHumidityPercentage()
        );
        var threshold = thresholdCommandService.handle(command);
        var body = DeviceThresholdResourceFromEntityAssembler.toResourceFromEntity(threshold);
        return ResponseEntity
                .created(URI.create("/api/v1/device-thresholds/" + threshold.getId()))
                .body(body);
    }
}
