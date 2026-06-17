package com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.analytics.domain.model.aggregates.Metric;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.DateRange;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.Incrementable;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricType;
import com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.entities.MetricPersistenceEntity;
import com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.entities.MetricValuePersistenceEntity;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.ArrayList;

public final class MetricPersistenceAssembler {

    private MetricPersistenceAssembler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converts a MetricPersistenceEntity from MongoDB to a Metric domain model.
     *
     * @param entity The MetricPersistenceEntity to convert. Must not be null.
     * @return A Metric domain model representing the given entity, or null if the input is null.
     */
    public static Metric toDomainFromPersistence(MetricPersistenceEntity entity) {
        if (entity == null) return null;

        var metric = new Metric();
        metric.setId(entity.getId());
        metric.setCategory(MetricCategory.valueOf(entity.getCategory()));
        metric.setType(MetricType.valueOf(entity.getType()));
        metric.setDateRange(new DateRange(entity.getDateRangeStart(), entity.getDateRangeEnd()));
        metric.setAccountId(new AccountId(entity.getAccountId()));
        metric.setLastRefreshedAt(entity.getLastRefreshedAt());

        var values = new ArrayList<Incrementable>();
        if (entity.getValues() != null) {
            for (var v : entity.getValues()) {
                values.add(new Incrementable(v.getValue(), v.getResourceId()) {});
            }
        }
        metric.setValues(values);

        return metric;
    }

    /**
     * Converts a Metric domain model to a MetricPersistenceEntity for MongoDB storage.
     *
     * @param metric The Metric domain model to convert. Must not be null.
     * @return A MetricPersistenceEntity representing the given Metric, or null if the input is null.
     */
    public static MetricPersistenceEntity toPersistenceFromDomain(Metric metric) {
        if (metric == null) return null;

        var entity = new MetricPersistenceEntity();

        if (metric.getId() != null) {
            entity.setId(metric.getId());
        }
        entity.setCategory(metric.getCategory().name());
        entity.setType(metric.getType().name());
        entity.setDateRangeStart(metric.getDateRange().getStartDate());
        entity.setDateRangeEnd(metric.getDateRange().getEndDate());
        entity.setAccountId(metric.getAccountId().getAccountId());
        entity.setLastRefreshedAt(metric.getLastRefreshedAt());

        var values = new ArrayList<MetricValuePersistenceEntity>();
        if (metric.getValues() != null) {
            for (var v : metric.getValues()) {
                values.add(new MetricValuePersistenceEntity(v.getValue(), v.getResourceId()));
            }
        }
        entity.setValues(values);

        return entity;
    }
}
