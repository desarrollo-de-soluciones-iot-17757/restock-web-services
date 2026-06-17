package com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Persistence entity representing a single metric value in a metric document.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricValuePersistenceEntity {

    /**
     * Numeric value of the metric data point.
     */
    private Long value;

    /**
     * Identifier of the resource associated with this metric value.
     */
    private String resourceId;
}
