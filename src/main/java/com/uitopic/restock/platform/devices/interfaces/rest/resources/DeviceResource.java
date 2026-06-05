package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DeviceResource", description = "Response resource representing a registered IoT device.")
public record DeviceResource(

        @Schema(description = "Device unique identifier")
        String id,

        @Schema(description = "Account (business) that owns this device")
        String accountId,

        @Schema(description = "Branch where the device is deployed")
        String branchId,

        @Schema(description = "Batch being monitored by this device")
        String assignedBatchId,

        @Schema(description = "Supply threshold configuration linked to this device")
        String supplyThresholdId,

        @Schema(description = "Device MAC address")
        String macAddress,

        @Schema(description = "Human-readable description")
        String description,

        @Schema(description = "Manufacturer name")
        String manufacturer,

        @Schema(description = "Device model")
        String model,

        @Schema(description = "Firmware version currently installed")
        String firmwareVersion,

        @Schema(description = "Net weight (product only, excluding container)")
        Double netWeight,

        @Schema(description = "Tare weight (empty container)")
        Double tareWeight,

        @Schema(description = "Gross weight (product + container)")
        Double grossWeight,

        @Schema(description = "Last calibration date")
        String calibrationDate,

        @Schema(description = "Weight unit name")
        String weightUnit,

        @Schema(description = "Weight unit abbreviation")
        String weightUnitAbbreviation,

        @Schema(description = "Stock units withdrawn physically but not through sales")
        Double justifiedWithdrawnStock,

        @Schema(description = "Device lifecycle status")
        String status
) {
}
