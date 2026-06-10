package com.uitopic.restock.platform.tracking.infrastructure.adapters;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;
import com.uitopic.restock.platform.tracking.domain.repositories.StockComparisonTaskRepository;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.assemblers.StockComparisonTaskPersistenceAssembler;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.repositories.StockComparisonTaskPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Implementation of the StockComparisonTaskRepository interface for MongoDB persistence.
 * This class uses the StockComparisonTaskPersistenceRepository to perform CRUD operations on StockComparisonTask entities
 */
@Repository
@RequiredArgsConstructor
public class StockComparisonTaskRepositoryImpl implements StockComparisonTaskRepository {

    // Repository for performing CRUD operations on StockComparisonTask entities in MongoDB
    private final StockComparisonTaskPersistenceRepository stockComparisonTaskMongoRepository;

    /**
     * @inheritDocs
     */
    @Override
    public StockComparisonTask save(StockComparisonTask task) {
        var saved = stockComparisonTaskMongoRepository
                .save(StockComparisonTaskPersistenceAssembler
                        .toPersistenceFromDomain(task));

        return StockComparisonTaskPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
