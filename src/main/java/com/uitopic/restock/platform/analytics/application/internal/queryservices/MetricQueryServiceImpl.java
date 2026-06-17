package com.uitopic.restock.platform.analytics.application.internal.queryservices;

import com.uitopic.restock.platform.analytics.domain.model.aggregates.Metric;
import com.uitopic.restock.platform.analytics.domain.model.queries.GetMetricsByAccountIdAndCategoryQuery;
import com.uitopic.restock.platform.analytics.domain.repositories.MetricRepository;
import com.uitopic.restock.platform.analytics.domain.services.MetricQueryService;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service for Metric read operations.
 * Handles metric retrieval by account and category.
 */
@Slf4j
@Service
public class MetricQueryServiceImpl implements MetricQueryService {

    private final MetricRepository metricRepository;

    public MetricQueryServiceImpl(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    /**
     * Retrieves metrics for a given account and category.
     *
     * @param query query with account identifier and metric category
     * @return list of matching metrics
     */
    @Override
    public List<Metric> handle(GetMetricsByAccountIdAndCategoryQuery query) {
        log.debug("Fetching metrics for account='{}' and category='{}'", query.accountId(), query.category());
        return metricRepository.findAllByAccountIdAndCategory(
                new AccountId(query.accountId()),
                query.category()
        );
    }

    /**
     * Finds a metric by its identifier.
     *
     * @param id metric identifier
     * @return metric if found
     */
    @Override
    public Optional<Metric> findById(String id) {
        log.debug("Fetching metric by id='{}'", id);
        return metricRepository.findById(id);
    }
}
