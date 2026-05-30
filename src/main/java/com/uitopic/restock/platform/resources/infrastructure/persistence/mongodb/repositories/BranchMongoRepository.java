package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Branch entities in MongoDB. This interface extends MongoRepository, providing basic CRUD operations and custom query methods for Branch entities.
 */
@Repository
public interface BranchMongoRepository extends MongoRepository<Branch, String> {

    /** Custom query method to find all branches associated with a specific account ID. This method returns a list of Branch entities that belong to the given account ID. */
    List<Branch> findByAccountId(AccountId accountId);

    /** Custom query method to check if a branch with the given name and account ID already exists. This method returns true if a branch with the specified name and account ID exists, false otherwise. */
    boolean existsByNameAndAccountId(String name, AccountId accountId);

    /** Custom query method to check if a branch with the given location already exists. This method returns true if a branch with the specified location exists, false otherwise. */
    boolean existsByLocation(String location);
}
