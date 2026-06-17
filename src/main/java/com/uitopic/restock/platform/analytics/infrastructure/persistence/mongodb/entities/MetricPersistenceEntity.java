package com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.entities;

import com.uitopic.restock.platform.shared.infrastructure.persistence.mongodb.entities.AuditableAbstractPersistenceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Persistence entity representing a metric document in MongoDB.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Document(collection = "metrics")
public class MetricPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * Category of the metric (e.g. INVENTORY, SALES).
     */
    private String category;

    /**
     * Type of metric within the category (e.g. STOCK_LEVEL, DAILY_REVENUE).
     */
    private String type;

    /**
     * List of metric values associated with this metric.
     */
    private List<MetricValuePersistenceEntity> values;

    /**
     * Timestamp of the last time this metric was refreshed.
     */
    private LocalDateTime lastRefreshedAt;

    /**
     * Start date of the date range this metric covers.
     */
    private LocalDate dateRangeStart;

    /**
     * End date of the date range this metric covers.
     */
    private LocalDate dateRangeEnd;

    /**
     * Account that owns this metric.
     */
    private String accountId;
}
