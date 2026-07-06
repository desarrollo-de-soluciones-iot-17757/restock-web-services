package com.uitopic.restock.platform.devices.interfaces.rest.controllers;

import com.uitopic.restock.platform.devices.application.internal.commandservices.DeviceHealthCommandService;
import com.uitopic.restock.platform.devices.interfaces.rest.resources.CreateDeviceHealthResource;
import com.uitopic.restock.platform.devices.interfaces.rest.resources.DeviceHealthResource;
import com.uitopic.restock.platform.devices.interfaces.rest.transform.CreateDeviceHealthCommandFromResourceAssembler;
import com.uitopic.restock.platform.devices.interfaces.rest.transform.DeviceHealthResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST Controller for receiving device health and microcontroller status metrics.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Device Health Receiver", description = "Endpoints for receiving device health status and microcontroller metrics.")
@RequiredArgsConstructor
public class DeviceHealthController {

    private final DeviceHealthCommandService deviceHealthCommandService;

    @PostMapping({"/devices-health", "/devices/status"})
    @Operation(
            summary = "Register device health metrics",
            description = "Receives device health status metrics (CPU, RAM, Voltage, Temperature) from Edge or embedded devices and stores it."
    )
    public ResponseEntity<DeviceHealthResource> registerDeviceHealth(
            @Valid @RequestBody CreateDeviceHealthResource resource
    ) {
        log.info("POST /api/v1/devices-health or /api/v1/devices/status - Received device health status: {}", resource);

        var command = CreateDeviceHealthCommandFromResourceAssembler.toCommandFromResource(resource);
        var deviceHealth = deviceHealthCommandService.handle(command);
        var responseResource = DeviceHealthResourceFromEntityAssembler.toResourceFromEntity(deviceHealth);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseResource);
    }
}
