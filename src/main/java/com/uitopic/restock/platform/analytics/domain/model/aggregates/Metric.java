package com.uitopic.restock.platform.analytics.domain.model.aggregates;

import com.uitopic.restock.platform.analytics.domain.model.valueobjects.DateRange;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.Incrementable;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricType;
import com.uitopic.restock.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain aggregate representing a tracked metric for analytics.
 * Each metric belongs to a category and type, holds incrementable values
 * within a date range, and is linked to a specific account.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Metric extends AbstractDomainAggregateRoot<Metric> {

    /** Unique identifier for this metric. */
    private String id;

    /** Category the metric belongs to (e.g. INVENTORY, SALES). */
    private MetricCategory category;

    /** Specific type of metric within the category (e.g. SALES_MADE). */
    private MetricType type;

    /** List of incrementable values recorded for this metric. */
    private List<Incrementable> values;

    /** Timestamp of the last time this metric was refreshed. */
    private LocalDateTime lastRefreshedAt;

    /** Date range covered by this metric. */
    private DateRange dateRange;

    /** Account that owns this metric. */
    private AccountId accountId;

    /**
     * Creates a new Metric with the specified properties.
     *
     * @param category  metric category (must not be null)
     * @param type      metric type (must not be null)
     * @param dateRange date range for the metric (must not be null)
     * @param accountId account identifier (must not be null or blank)
     */
    @Builder
    public Metric(MetricCategory category, MetricType type, DateRange dateRange, String accountId) {
        if (category == null) throw new IllegalArgumentException("category cannot be null");
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        if (dateRange == null) throw new IllegalArgumentException("dateRange cannot be null");
        if (accountId == null || accountId.isBlank()) throw new IllegalArgumentException("accountId cannot be null or blank");
        this.category = category;
        this.type = type;
        this.dateRange = dateRange;
        this.accountId = new AccountId(accountId);
        this.values = new ArrayList<>();
        this.lastRefreshedAt = LocalDateTime.now();
    }

    /**
     * Adds an incrementable value to this metric.
     *
     * @param value value to add (must not be null)
     */
    public void addValue(Incrementable value) {
        if (value == null) throw new IllegalArgumentException("value cannot be null");
        this.values.add(value);
        this.lastRefreshedAt = LocalDateTime.now();
    }

    /**
     * Returns the list of incrementable values.
     *
     * @return list of incrementable values
     */
    public List<Incrementable> getValues() {
        return values;
    }
}
