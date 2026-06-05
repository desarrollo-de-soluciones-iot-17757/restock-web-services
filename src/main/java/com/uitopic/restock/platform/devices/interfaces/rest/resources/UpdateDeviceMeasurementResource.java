package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

@Schema(description = "Request body to configure device weight measurement parameters.")
public record UpdateDeviceMeasurementResource(

        @Schema(description = "Net weight (product only, without container)")
        @NotNull(message = "Net weight is required")
        @PositiveOrZero(message = "Net weight must be zero or positive")
        Double netWeight,

        @Schema(description = "Tare weight (empty container)")
        @NotNull(message = "Tare weight is required")
        @PositiveOrZero(message = "Tare weight must be zero or positive")
        Double tareWeight,

        @Schema(description = "Gross weight (product + container)")
        @NotNull(message = "Gross weight is required")
        @PositiveOrZero(message = "Gross weight must be zero or positive")
        Double grossWeight,

        @Schema(description = "Date of the last calibration")
        @NotNull(message = "Calibration date is required")
        LocalDate calibrationDate,

        @Schema(description = "Weight unit name (e.g., kilogram, gram)")
        @NotBlank(message = "Weight unit name is required")
        String weightUnitName,

        @Schema(description = "Weight unit abbreviation (optional, auto-generated if absent)")
        String weightUnitAbbreviation
) {
}
