package com.uitopic.restock.platform.analytics.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "MetricResource",
    description = "Represents a metric used for analytics purposes."
)
public record MetricResource() {
}
