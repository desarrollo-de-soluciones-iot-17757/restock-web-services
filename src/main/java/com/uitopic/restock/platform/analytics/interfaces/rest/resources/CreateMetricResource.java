package com.uitopic.restock.platform.analytics.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/** Request resource to register a new metric. */
@Schema(name = "CreateMetricResource", description = "Request resource to register a new metric.")
public record CreateMetricResource(

        /** Metric category (INVENTORY, WORKERS, NOTIFICATIONS, SALES). */
        @NotBlank
        @Schema(description = "Metric category (INVENTORY, WORKERS, NOTIFICATIONS, SALES)")
        String category,

        /** Metric type (e.g. SUPPLIES_CREATED, SALES_MADE). */
        @NotBlank
        @Schema(description = "Metric type (e.g. SUPPLIES_CREATED, SALES_MADE)")
        String type,

        /** Date range start. */
        @NotNull
        @Schema(description = "Date range start")
        LocalDate dateRangeStart,

        /** Date range end. */
        @NotNull
        @Schema(description = "Date range end")
        LocalDate dateRangeEnd,

        /** Account ID. */
        @NotBlank
        @Schema(description = "Account ID")
        String accountId
) {
}
