package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.BranchMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link BranchRepository} that uses MongoDB for data storage
 * within the resources bounded context.
 *
 * <p>Acts as a bridge between the domain layer and the MongoDB persistence layer,
 * adapting {@link BranchMongoRepository} to the {@link BranchRepository} port.
 * This keeps the domain layer free of Spring Data dependencies.
 */
@Repository
public class BranchRepositoryImpl implements BranchRepository {

    /** The underlying Spring Data MongoDB repository for {@link Branch} documents. */
    private final BranchMongoRepository mongo;

    /**
     * Constructs a {@code BranchRepositoryImpl} with the given MongoDB repository.
     *
     * @param mongo the Spring Data MongoDB repository to delegate to
     */
    public BranchRepositoryImpl(BranchMongoRepository mongo) {
        this.mongo = mongo;
    }

    /**
     * Persists a {@link Branch} aggregate to MongoDB.
     *
     * @param branch the branch to save
     * @return the saved branch, including any auto-generated ID
     */
    @Override
    public Branch save(Branch branch) {
        return mongo.save(branch);
    }

    /**
     * Finds a branch by its unique identifier.
     *
     * @param id the unique identifier of the branch
     * @return an {@link Optional} containing the {@link Branch} if found, or empty if not found
     */
    @Override
    public Optional<Branch> findById(String id) {
        return mongo.findById(id);
    }

    /**
     * Finds all branches belonging to the specified account.
     *
     * @param accountId the account whose branches are to be retrieved
     * @return a {@link List} of {@link Branch} aggregates for that account
     */
    @Override
    public List<Branch> findByAccountId(AccountId accountId) {
        return mongo.findByAccountId(accountId);
    }

    /**
     * Removes a branch document from MongoDB by its unique identifier.
     *
     * @param id the unique identifier of the branch to delete
     */
    @Override
    public void deleteById(String id) {
        mongo.deleteById(id);
    }

    /**
     * Checks whether a branch with the given name already exists within the specified account.
     *
     * @param name      the branch name to check
     * @param accountId the account scope for the uniqueness check
     * @return {@code true} if a branch with that name exists in the account, {@code false} otherwise
     */
    @Override
    public boolean existsByNameAndAccountId(String name, AccountId accountId) {
        return mongo.existsByNameAndAccountId(name, accountId);
    }
}
