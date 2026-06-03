package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.entities.BranchPersistenceEntity;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository for Branch documents.
 *
 * Extends Spring Data MongoRepository to provide default CRUD operations and
 * custom query methods for the branches collection.
 */
@Repository
public interface BranchPersistenceRepository extends MongoRepository<BranchPersistenceEntity, String> {

    /**
     * Finds all branches that belong to an account.
     *
     * @param accountId account identifier
     * @return branches owned by the account
     */
    List<BranchPersistenceEntity> findByAccountId(AccountId accountId);

    /**
     * Checks whether a branch name already exists in an account.
     *
     * @param name branch name
     * @param accountId account identifier
     * @return true if the branch name already exists in the account
     */
    boolean existsByNameAndAccountId(String name, AccountId accountId);

    /**
     * Checks whether another branch with the same name exists in an account,
     * excluding a specific branch identifier.
     *
     * @param name branch name
     * @param accountId account identifier
     * @param id branch identifier to exclude
     * @return true if another branch with the same name exists
     */
    boolean existsByNameAndAccountIdAndIdNot(String name, AccountId accountId, String id);
}