package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.CustomSupplyMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link CustomSupplyRepository} that uses MongoDB for data storage
 * within the resources bounded context.
 *
 * <p>Acts as a bridge between the domain layer and the MongoDB persistence layer,
 * adapting {@link CustomSupplyMongoRepository} to the {@link CustomSupplyRepository} port.
 * This keeps the domain layer free of Spring Data dependencies.
 */
@Repository
public class CustomSupplyRepositoryImpl implements CustomSupplyRepository {

    /** The underlying Spring Data MongoDB repository for {@link CustomSupply} documents. */
    private final CustomSupplyMongoRepository customSupplyMongoRepository;

    /**
     * Constructs a {@code CustomSupplyRepositoryImpl} with the given MongoDB repository.
     *
     * @param customSupplyMongoRepository the Spring Data MongoDB repository to delegate to
     */
    public CustomSupplyRepositoryImpl(CustomSupplyMongoRepository customSupplyMongoRepository) {
        this.customSupplyMongoRepository = customSupplyMongoRepository;
    }

    /**
     * Finds all custom supplies belonging to the specified account.
     *
     * @param accountId the account whose custom supplies are to be retrieved
     * @return a {@link List} of {@link CustomSupply} aggregates for that account
     */
    @Override public List<CustomSupply> findByAccountId(AccountId accountId) { return customSupplyMongoRepository.findByAccountId(accountId); }

    /**
     * Finds a custom supply by its unique identifier.
     *
     * @param id the unique identifier of the custom supply
     * @return an {@link Optional} containing the {@link CustomSupply} if found, or empty if not found
     */
    @Override public Optional<CustomSupply> findById(String id) { return customSupplyMongoRepository.findById(id); }

    /**
     * Checks whether a custom supply with the given name already exists within the specified account.
     *
     * @param accountId the account scope for the uniqueness check
     * @param name      the supply name to check
     * @return {@code true} if a custom supply with that name exists in the account, {@code false} otherwise
     */
    @Override public Boolean existsByAccountIdAndName(AccountId accountId, String name) { return customSupplyMongoRepository.existsByAccountIdAndName(accountId, name); }

    /**
     * Persists a {@link CustomSupply} aggregate to MongoDB.
     *
     * @param cs the custom supply to save
     * @return the saved custom supply, including any auto-generated ID
     */
    @Override public CustomSupply save(CustomSupply cs) { return customSupplyMongoRepository.save(cs); }

    /**
     * Removes a custom supply document from MongoDB by its unique identifier.
     *
     * @param id the unique identifier of the custom supply to delete
     */
    @Override public void deleteById(String id) { customSupplyMongoRepository.deleteById(id); }
}
