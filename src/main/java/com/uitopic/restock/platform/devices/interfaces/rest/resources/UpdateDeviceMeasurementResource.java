package com.uitopic.restock.platform.devices.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

@Schema(description = "Request body to configure device weight measurement parameters.")
public record UpdateDeviceMeasurementResource(

        @Schema(description = "Weight represented by one stock unit")
        @NotNull(message = "Unit stock weight is required")
        @Positive(message = "Unit stock weight must be greater than zero")
        @JsonAlias({"unitWeight", "netWeight"})
        Double unitStockWeight,

        @Schema(description = "Tare weight (empty container)")
        @NotNull(message = "Tare weight is required")
        @PositiveOrZero(message = "Tare weight must be zero or positive")
        Double tareWeight,

        @Schema(description = "Gross weight (product + container)")
        @PositiveOrZero(message = "Gross weight must be zero or positive")
        Double grossWeight,

        @Schema(description = "Date of the last calibration")
        LocalDate calibrationDate,

        @Schema(description = "Weight unit name (e.g., kilogram, gram)")
        @NotBlank(message = "Weight unit name is required")
        String weightUnitName,

        @Schema(description = "Weight unit abbreviation (optional, auto-generated if absent)")
        String weightUnitAbbreviation
) {
}
