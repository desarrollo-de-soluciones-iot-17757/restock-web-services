package com.uitopic.restock.platform.analytics.application.internal.commandservices;

import com.uitopic.restock.platform.analytics.domain.model.aggregates.Metric;
import com.uitopic.restock.platform.analytics.domain.model.commands.RegisterMetricCommand;
import com.uitopic.restock.platform.analytics.domain.repositories.MetricRepository;
import com.uitopic.restock.platform.analytics.domain.services.MetricCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Application service for Metric write operations.
 * Handles metric registration.
 */
@Slf4j
@Service
public class MetricCommandServiceImpl implements MetricCommandService {

    private final MetricRepository metricRepository;

    public MetricCommandServiceImpl(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    /**
     * Registers a new metric.
     *
     * @param command command with the metric data
     * @return registered metric
     */
    @Override
    public Metric handle(RegisterMetricCommand command) {
        log.info("Registering metric with category='{}', type='{}'", command.category(), command.type());

        var metric = Metric.builder()
                .category(command.category())
                .type(command.type())
                .dateRange(command.dateRange())
                .accountId(command.accountId())
                .build();

        var saved = metricRepository.save(metric);
        log.info("Metric registered successfully: id='{}'", saved.getId());
        return saved;
    }
}
