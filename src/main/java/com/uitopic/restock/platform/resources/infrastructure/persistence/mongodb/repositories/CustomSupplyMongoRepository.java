package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository interface for managing CustomSupply aggregates.
 * This interface extends the Spring Data MongoRepository, providing CRUD operations and custom query methods for CustomSupply aggregates in a MongoDB database.
 */
@Repository
public interface CustomSupplyMongoRepository extends MongoRepository<CustomSupply, String> {

    /**
     * Finds a list of CustomSupply aggregates by the given account ID.
     *
     * @param accountId the account ID for which to fetch the custom supplies
     * @return a list of CustomSupply aggregates that are associated with the specified account ID
     */
    List<CustomSupply> findByAccountId(AccountId accountId);

    /**
     * Checks if a CustomSupply with the given account ID and name already exists.
     *
     * @param accountId the account ID to check for
     * @param name the name of the CustomSupply to check for
     * @return true if a CustomSupply with the specified account ID and name exists, false otherwise
     */
    Boolean existsByAccountIdAndName(AccountId accountId, String name);
}
