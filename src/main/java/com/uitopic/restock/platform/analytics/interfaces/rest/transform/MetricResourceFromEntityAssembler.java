package com.uitopic.restock.platform.analytics.interfaces.rest.transform;

import com.uitopic.restock.platform.analytics.domain.model.aggregates.Metric;
import com.uitopic.restock.platform.analytics.interfaces.rest.resources.MetricResource;
import com.uitopic.restock.platform.analytics.interfaces.rest.resources.MetricValueResource;

import java.util.List;

/** Assembler to convert Metric domain model into MetricResource. */
public class MetricResourceFromEntityAssembler {

    private MetricResourceFromEntityAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /** Converts a Metric entity to a MetricResource DTO. */
    public static MetricResource toResourceFromEntity(Metric metric) {
        if (metric == null) return null;

        var values = metric.getValues() != null
                ? metric.getValues().stream()
                .map(v -> new MetricValueResource(v.getValue(), v.getResourceId()))
                .toList()
                : List.<MetricValueResource>of();

        return new MetricResource(
                metric.getId(),
                metric.getCategory().name(),
                metric.getType().name(),
                values,
                metric.getLastRefreshedAt(),
                metric.getDateRange().getStartDate(),
                metric.getDateRange().getEndDate(),
                metric.getAccountId().getAccountId()
        );
    }
}
