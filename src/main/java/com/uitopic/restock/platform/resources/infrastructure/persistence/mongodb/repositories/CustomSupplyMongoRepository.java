package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository for {@link CustomSupply} aggregates.
 *
 * <p>Extends Spring Data's {@link MongoRepository} to provide standard CRUD operations
 * and custom query methods for the {@code custom_supplies} collection. Used exclusively by
 * {@link com.uitopic.restock.platform.resources.infrastructure.repositories.CustomSupplyRepositoryImpl}
 * as the underlying persistence mechanism.
 */
@Repository
public interface CustomSupplyMongoRepository extends MongoRepository<CustomSupply, String> {

    /**
     * Finds all custom supplies associated with the specified account ID.
     *
     * @param accountId the account whose custom supplies are to be retrieved
     * @return a {@link List} of {@link CustomSupply} aggregates for that account
     */
    List<CustomSupply> findByAccountId(AccountId accountId);

    /**
     * Checks whether a custom supply with the given name already exists within the specified account.
     * Used to enforce name uniqueness per account before creating a new custom supply.
     *
     * @param accountId the account scope for the uniqueness check
     * @param name      the supply name to check
     * @return {@code true} if a custom supply with that name exists in the account, {@code false} otherwise
     */
    Boolean existsByAccountIdAndName(AccountId accountId, String name);
}
