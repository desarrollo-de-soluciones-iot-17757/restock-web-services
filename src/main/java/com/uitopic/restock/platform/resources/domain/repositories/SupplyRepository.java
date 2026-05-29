package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;

import java.util.List;

/**
 * Repository non-framework interface for managing Supply entities.
 * This interface defines the contract for data access operations related to Supply entities, allowing for flexibility in implementation.
 * Implementations of this interface can use various data storage technologies (e.g., relational databases, NoSQL databases, in-memory storage) without being tied to a specific framework.
 */
public interface SupplyRepository {

    /**
     * Retrieves all Supply entities from the data source.
     *
     * @return a list of all Supply entities available in the data source
     */
    List<Supply> findAll();
}
