package com.uitopic.restock.platform.resources.infrastructure.adapters;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.assemblers.CustomSupplyPersistenceAssembler;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.CustomSupplyPersistenceRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the CustomSupplyRepository interface that uses a MongoDB persistence repository.
 * This adapter translates between the domain model and the persistence model, allowing the application to interact with the database without being coupled to MongoDB-specific details.
 */
@Repository
public class CustomSupplyRepositoryImpl implements CustomSupplyRepository {

    // The persistence repository for CustomSupply entities
    private final CustomSupplyPersistenceRepository customSupplyPersistenceRepository;

    // Constructor injection of the persistence repository
    public CustomSupplyRepositoryImpl(CustomSupplyPersistenceRepository customSupplyPersistenceRepository) {
        this.customSupplyPersistenceRepository = customSupplyPersistenceRepository;
    }

    /**
     * Saves a custom supply to the repository.
     *
     * @param customSupply the custom supply to save
     * @return the saved custom supply with any generated fields (e.g., id) populated
     */
    @Override
    public CustomSupply save(CustomSupply customSupply) {
        var saved = customSupplyPersistenceRepository.save(CustomSupplyPersistenceAssembler.toPersistenceFromDomain(customSupply));
        return CustomSupplyPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Finds all custom supplies that belong to an account.
     *
     * @param accountId account identifier
     * @return custom supplies owned by the account
     */
    @Override
    public List<CustomSupply> findByAccountId(AccountId accountId) {
        return customSupplyPersistenceRepository.findByAccountId(accountId)
                .stream()
                .map(CustomSupplyPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    /**
     * Finds a custom supply by its unique identifier.
     *
     * @param id custom supply identifier
     * @return an Optional containing the custom supply if found, or empty if not found
     */
    @Override
    public Optional<CustomSupply> findById(String id) {
        return customSupplyPersistenceRepository.findById(id)
                .map(CustomSupplyPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Finds a custom supply by its unique identifier.
     *
     * @return the custom supply with the given id, or null if not found
     */
    @Override
    public List<CustomSupply> findAll() {
        return customSupplyPersistenceRepository.findAll()
                .stream()
                .map(CustomSupplyPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    /**
     * Checks whether a custom supply name already exists within an account.
     *
     * @param accountId account identifier
     * @param name      custom supply name
     * @return true if the name already exists
     */
    @Override
    public boolean existsByAccountIdAndName(AccountId accountId, String name) {
        return customSupplyPersistenceRepository.existsByAccountIdAndName(accountId, name);
    }

    /**
     * Checks whether another custom supply with the same name exists in an account.
     *
     * @param accountId account identifier
     * @param name      custom supply name
     * @param id        custom supply id to exclude
     * @return true if another custom supply with the same name exists
     */
    @Override
    public boolean existsByAccountIdAndNameAndIdNot(AccountId accountId, String name, String id) {
        return customSupplyPersistenceRepository.existsByAccountIdAndNameAndIdNot(accountId, name, id);
    }

    /**
     * Checks whether a custom supply already exists for an account and base supply.
     *
     * @param accountId account identifier
     * @param supplyId  base supply identifier
     * @return true if the custom supply exists
     */
    @Override
    public boolean existsByAccountIdAndSupplyId(AccountId accountId, String supplyId) {
        return customSupplyPersistenceRepository.existsByAccountIdAndSupplyId(accountId, supplyId);
    }

    /**
     * Checks whether another custom supply already exists for an account and base supply.
     *
     * @param accountId account identifier
     * @param supplyId  base supply identifier
     * @param id        current custom supply identifier to exclude
     * @return true if another custom supply exists
     */
    @Override
    public boolean existsByAccountIdAndSupplyIdAndIdNot(AccountId accountId, String supplyId, String id) {
        return customSupplyPersistenceRepository.existsByAccountIdAndSupplyIdAndIdNot(accountId, supplyId, id);
    }

    /**
     * Deletes a custom supply by its unique identifier.
     *
     * @param id custom supply identifier
     */
    @Override
    public void deleteById(String id) {
        customSupplyPersistenceRepository.deleteById(id);
    }
}
