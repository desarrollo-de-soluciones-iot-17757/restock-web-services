package com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.analytics.infrastructure.persistence.mongodb.entities.MetricPersistenceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for accessing metric documents.
 */
@Repository
public interface MetricPersistenceRepository extends MongoRepository<MetricPersistenceEntity, String> {

    /**
     * Finds all metrics by account ID and category.
     *
     * @param accountId identifier of the account
     * @param category category of the metric
     * @return list of matching metrics
     */
    List<MetricPersistenceEntity> findAllByAccountIdAndCategory(String accountId, String category);

    /**
     * Finds all metrics by account ID.
     *
     * @param accountId identifier of the account
     * @return list of metrics owned by the account
     */
    List<MetricPersistenceEntity> findAllByAccountId(String accountId);
}
