package com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;
import com.uitopic.restock.platform.tracking.infrastructure.persistence.mongodb.entities.StockComparisonTaskPersistenceEntity;

/**
 * Assembler class responsible for converting between StockComparisonTask domain objects and StockComparisonTaskPersistenceEntity objects used for MongoDB persistence. This class provides static methods to facilitate the mapping of properties between the two representations, ensuring that data can be seamlessly transferred between the domain model and the persistence layer.
 */
public final class StockComparisonTaskPersistenceAssembler {

    private StockComparisonTaskPersistenceAssembler() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a StockComparisonTaskPersistenceEntity from MongoDB to a StockComparisonTask domain object. This method maps the properties of the persistence entity to the corresponding fields in the domain object, allowing the application to work with the data in its domain model form.
     *
     * @param entity the StockComparisonTaskPersistenceEntity retrieved from MongoDB, which contains the details of the stock comparison task, including the physical and system stock records, device ID, status, and result of the comparison
     * @return a StockComparisonTask domain object that represents the same stock comparison task as the input persistence entity, ready to be used in the application. If the input entity is null, this method returns null.
     */
    public static StockComparisonTask toDomainFromPersistence(StockComparisonTaskPersistenceEntity entity) {
        if (entity == null) return null;

        var stockComparisonTask = new StockComparisonTask();
        stockComparisonTask.setId(entity.getId());
        stockComparisonTask.setStatus(entity.getStatus());
        stockComparisonTask.setDeviceId(entity.getDeviceId());
        stockComparisonTask.setCreatedAt(entity.getCreatedAt().toInstant());
        stockComparisonTask.setPhysicalStock(entity.getPhysicalStock());
        stockComparisonTask.setSystemStock(entity.getSystemStock());
        stockComparisonTask.setResult(entity.getResult());

        return stockComparisonTask;
    }

    /**
     * Converts a StockComparisonTask domain object to a StockComparisonTaskPersistenceEntity for storage in MongoDB. This method maps the properties of the domain object to the corresponding fields in the persistence entity, ensuring that all relevant information is preserved for later retrieval and use.
     *
     * @param stockComparisonTask the StockComparisonTask domain object to be converted to a persistence entity, which contains the details of the stock comparison task, including the physical and system stock records, device ID, status, and result of the comparison
     * @return a StockComparisonTaskPersistenceEntity that represents the same stock comparison task as the input domain object, ready to be stored in MongoDB. If the input domain object is null, this method returns null.
     */
    public static StockComparisonTaskPersistenceEntity toPersistenceFromDomain(StockComparisonTask stockComparisonTask) {
        if (stockComparisonTask == null) return null;

        var entity = new StockComparisonTaskPersistenceEntity();
        if (stockComparisonTask.getId() != null) {
            entity.setId(stockComparisonTask.getId());
        }
        entity.setStatus(stockComparisonTask.getStatus());
        entity.setDeviceId(stockComparisonTask.getDeviceId());
        entity.setPhysicalStock(stockComparisonTask.getPhysicalStock());
        entity.setSystemStock(stockComparisonTask.getSystemStock());
        entity.setResult(stockComparisonTask.getResult());

        return entity;
    }
}
