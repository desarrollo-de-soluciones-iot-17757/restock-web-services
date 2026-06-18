package com.uitopic.restock.platform.analytics.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/** Represents a single metric value entry. */
@Schema(name = "MetricValueResource", description = "Represents a single metric value entry.")
public record MetricValueResource(
        /** Numeric value of the metric. */
        @Schema(description = "Numeric value of the metric")
        Long value,

        /** ID of the resource this metric value is associated with. */
        @Schema(description = "ID of the resource this metric value is associated with")
        String resourceId
) {
}
