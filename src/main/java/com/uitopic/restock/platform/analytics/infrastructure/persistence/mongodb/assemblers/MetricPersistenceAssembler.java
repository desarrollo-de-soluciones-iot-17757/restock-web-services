package com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.analytics.domain.model.aggregates.Metric;
import com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.entities.MetricPersistenceEntity;

public final class MetricPersistenceAssembler {

    private MetricPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    public static Metric toDomainFromPersistence(MetricPersistenceEntity entity) {
        return null;
    }

    public static MetricPersistenceEntity toPersistenceFromDomain(Metric metric) {
        return null;
    }
}
