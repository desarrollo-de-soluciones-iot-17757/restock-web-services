package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository for {@link Branch} aggregates within the resources bounded context.
 *
 * <p>Extends Spring Data's {@link MongoRepository} to provide standard CRUD operations
 * and custom query methods for the {@code branches} collection. Used exclusively by
 * {@link com.uitopic.restock.platform.resources.infrastructure.repositories.BranchRepositoryImpl}
 * as the underlying persistence mechanism.
 */
@Repository
public interface BranchMongoRepository extends MongoRepository<Branch, String> {

    /**
     * Finds all branches associated with the specified account ID.
     *
     * @param accountId the account whose branches are to be retrieved
     * @return a {@link List} of {@link Branch} aggregates for that account
     */
    List<Branch> findByAccountId(AccountId accountId);

    /**
     * Checks whether a branch with the given name already exists within the specified account.
     * Used to enforce name uniqueness per account before creating or renaming a branch.
     *
     * @param name      the branch name to check
     * @param accountId the account scope for the uniqueness check
     * @return {@code true} if a branch with that name exists in the account, {@code false} otherwise
     */
    boolean existsByNameAndAccountId(String name, AccountId accountId);
}
