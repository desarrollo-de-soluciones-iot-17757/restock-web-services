package com.uitopic.restock.platform.devices.interfaces.rest.controllers;

import com.uitopic.restock.platform.devices.domain.model.commands.*;
import com.uitopic.restock.platform.devices.domain.model.queries.*;
import com.uitopic.restock.platform.devices.domain.services.DeviceCommandService;
import com.uitopic.restock.platform.devices.domain.services.DeviceQueryService;
import com.uitopic.restock.platform.devices.interfaces.rest.resources.*;
import com.uitopic.restock.platform.devices.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
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
@RequestMapping(value = "/api/v1/devices", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Devices", description = "IoT device registration, configuration, and query endpoints.")
public class DevicesController {

    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    public DevicesController(DeviceCommandService deviceCommandService, DeviceQueryService deviceQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }

    @Operation(summary = "Get devices by account")
    @GetMapping
    public ResponseEntity<List<DeviceResource>> getAll(@RequestParam String accountId) {
        var devices = deviceQueryService.handle(new GetDevicesByAccountIdQuery(accountId));
        var resources = devices.stream()
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get device by ID")
    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceResource> getById(@PathVariable String deviceId) {
        var device = deviceQueryService.handle(new GetDeviceByIdQuery(deviceId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found: " + deviceId));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Register a new device (onboarding step 1)")
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResource> register(@Valid @RequestBody CreateDeviceResource resource) {
        var command = new RegisterDeviceCommand(resource.macAddress(), resource.accountId(), resource.description());
        var device = deviceCommandService.handle(command);
        var body = DeviceResourceFromEntityAssembler.toResourceFromEntity(device);
        return ResponseEntity
                .created(URI.create("/api/v1/devices/" + device.getId()))
                .body(body);
    }

    @Operation(summary = "Add technical specifications to a device (onboarding step 2)")
    @PatchMapping(value = "/{deviceId}/specifications", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResource> addSpecifications(
            @PathVariable String deviceId,
            @Valid @RequestBody AddDeviceSpecificationsResource resource
    ) {
        var command = new AddDeviceSpecificationsCommand(
                deviceId, resource.manufacturer(), resource.model(), resource.firmwareVersion()
        );
        var device = deviceCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found: " + deviceId));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Assign device to a branch (onboarding step 3)")
    @PatchMapping(value = "/{deviceId}/configuration/branch", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResource> assignBranch(
            @PathVariable String deviceId,
            @Valid @RequestBody AssignBranchResource resource
    ) {
        var command = new AssignDeviceToBranchCommand(deviceId, resource.branchId());
        var device = deviceCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found: " + deviceId));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Assign a batch to monitor (onboarding step 4)")
    @PatchMapping(value = "/{deviceId}/configuration/batch", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResource> assignBatch(
            @PathVariable String deviceId,
            @Valid @RequestBody AssignBatchResource resource
    ) {
        var command = new AssignBatchToDeviceCommand(deviceId, resource.batchId());
        var device = deviceCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found: " + deviceId));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Link a supply threshold (onboarding step 5)")
    @PatchMapping(value = "/{deviceId}/configuration/threshold", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResource> assignSupplyThreshold(
            @PathVariable String deviceId,
            @Valid @RequestBody AssignSupplyThresholdResource resource
    ) {
        var command = new AssignSupplyThresholdCommand(deviceId, resource.supplyThresholdId());
        var device = deviceCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found: " + deviceId));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Configure weight measurement parameters (onboarding step 6)")
    @PatchMapping(value = "/{deviceId}/configuration/measurement", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResource> updateMeasurement(
            @PathVariable String deviceId,
            @Valid @RequestBody UpdateDeviceMeasurementResource resource
    ) {
        var command = new UpdateDeviceMeasurementCommand(
                deviceId,
                resource.unitStockWeight(),
                resource.tareWeight(),
                resource.grossWeight(),
                resource.calibrationDate(),
                resource.weightUnitName(),
                resource.weightUnitAbbreviation()
        );
        var device = deviceCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found: " + deviceId));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Transition device status (CALIBRATED: completes onboarding · INACTIVE: deactivates device)")
    @PatchMapping(value = "/{deviceId}/status", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResource> updateStatus(
            @PathVariable String deviceId,
            @Valid @RequestBody UpdateDeviceStatusResource resource
    ) {
        var device = switch (resource.status()) {
            case "CALIBRATED" -> {
                var command = new ConfirmDeviceConfigurationCommand(deviceId);
                yield deviceCommandService.handle(command)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found: " + deviceId));
            }
            case "INACTIVE" -> {
                var command = new DeactivateDeviceCommand(deviceId);
                yield deviceCommandService.handle(command)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found: " + deviceId));
            }
            default -> throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Unsupported status transition: " + resource.status());
        };
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Update justified withdrawn stock for a device")
    @PatchMapping(value = "/{deviceId}/withdrawn-stock", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResource> updateWithdrawnStock(
            @PathVariable String deviceId,
            @Valid @RequestBody UpdateJustifiedWithdrawnStockResource resource
    ) {
        var command = new UpdateJustifiedWithdrawnStockCommand(deviceId, resource.amount());
        var device = deviceCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found: " + deviceId));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }
}
