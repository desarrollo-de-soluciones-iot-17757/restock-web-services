package com.uitopic.restock.platform.analytics.infrastructure.adapters;

import com.uitopic.restock.platform.analytics.domain.model.aggregates.Metric;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.uitopic.restock.platform.analytics.domain.repositories.MetricRepository;
import com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.assemblers.MetricPersistenceAssembler;
import com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.repositories.MetricPersistenceRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of the MetricRepository domain port.
 */
@Repository
public class MetricRepositoryImpl implements MetricRepository {

    /**
     * Spring Data MongoDB repository used to access metric documents.
     */
    private final MetricPersistenceRepository metricMongoRepository;

    /**
     * Creates a MetricRepositoryImpl with the required MongoDB repository.
     *
     * @param metricMongoRepository MongoDB repository for metrics
     */
    public MetricRepositoryImpl(MetricPersistenceRepository metricMongoRepository) {
        this.metricMongoRepository = metricMongoRepository;
    }

    /**
     * Saves a metric.
     *
     * @param metric metric to save
     * @return saved metric
     */
    @Override
    public Metric save(Metric metric) {
        var saved = metricMongoRepository.save(MetricPersistenceAssembler.toPersistenceFromDomain(metric));
        return MetricPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Finds a metric by its identifier.
     *
     * @param id metric identifier
     * @return metric if found
     */
    @Override
    public Optional<Metric> findById(String id) {
        return metricMongoRepository.findById(id)
                .map(MetricPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Finds all metrics filtered by account ID and category.
     *
     * @param accountId account identifier
     * @param category metric category
     * @return list of matching metrics
     */
    @Override
    public List<Metric> findAllByAccountIdAndCategory(AccountId accountId, MetricCategory category) {
        return metricMongoRepository
                .findAllByAccountIdAndCategory(accountId.getAccountId(), category.name())
                .stream()
                .map(MetricPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    /**
     * Finds all metrics by account ID.
     *
     * @param accountId account identifier
     * @return metrics owned by the account
     */
    @Override
    public List<Metric> findAllByAccountId(AccountId accountId) {
        return metricMongoRepository
                .findAllByAccountId(accountId.getAccountId())
                .stream()
                .map(MetricPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    /**
     * Deletes a metric by its identifier.
     *
     * @param id metric identifier
     */
    @Override
    public void deleteById(String id) {
        metricMongoRepository.deleteById(id);
    }
}
