package com.uitopic.restock.platform.analytics.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** Response resource representing a metric used for analytics purposes. */
@Schema(
    name = "MetricResource",
    description = "Represents a metric used for analytics purposes."
)
public record MetricResource(
        /** Metric unique identifier. */
        @Schema(description = "Metric unique identifier")
        String id,

        /** Metric category. */
        @Schema(description = "Metric category")
        String category,

        /** Metric type. */
        @Schema(description = "Metric type")
        String type,

        /** Metric values. */
        @Schema(description = "Metric values")
        List<MetricValueResource> values,

        /** Last refreshed timestamp. */
        @Schema(description = "Last refreshed timestamp")
        LocalDateTime lastRefreshedAt,

        /** Date range start. */
        @Schema(description = "Date range start")
        LocalDate dateRangeStart,

        /** Date range end. */
        @Schema(description = "Date range end")
        LocalDate dateRangeEnd,

        /** Account ID. */
        @Schema(description = "Account ID")
        String accountId
) {
}
