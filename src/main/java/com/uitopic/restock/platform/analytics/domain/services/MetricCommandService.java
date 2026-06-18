package com.uitopic.restock.platform.analytics.domain.services;

import com.uitopic.restock.platform.analytics.domain.model.aggregates.Metric;
import com.uitopic.restock.platform.analytics.domain.model.commands.RegisterMetricCommand;

/**
 * Domain service interface for metric write operations.
 */
public interface MetricCommandService {

    /**
     * Handles the registration of a new metric.
     *
     * @param command command with the metric data
     * @return registered metric
     */
    Metric handle(RegisterMetricCommand command);
}
