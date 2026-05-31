package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository port for {@link CustomSupply} aggregate persistence within the resources bounded context.
 *
 * <p>Defines the contract for storing and retrieving custom supplies, decoupling the domain
 * layer from the MongoDB infrastructure. The implementation is provided by
 * {@link com.uitopic.restock.platform.resources.infrastructure.repositories.CustomSupplyRepositoryImpl}.
 */
public interface CustomSupplyRepository {

    /**
     * Finds all custom supplies belonging to the specified account.
     *
     * @param accountId the account whose custom supplies are to be retrieved
     * @return a {@link List} of {@link CustomSupply} aggregates for that account
     */
    List<CustomSupply> findByAccountId(AccountId accountId);

    /**
     * Finds a custom supply by its unique identifier.
     *
     * @param id the unique identifier of the custom supply
     * @return an {@link Optional} containing the {@link CustomSupply} if found, or empty if not found
     */
    Optional<CustomSupply> findById(String id);

    /**
     * Checks whether a custom supply with the given name already exists within the specified account.
     * Used to enforce name uniqueness per account before creating a new custom supply.
     *
     * @param accountId the account scope for the uniqueness check
     * @param name      the supply name to check
     * @return {@code true} if a custom supply with that name exists in the account, {@code false} otherwise
     */
    Boolean existsByAccountIdAndName(AccountId accountId, String name);

    /**
     * Persists a {@link CustomSupply} aggregate. Creates a new document if the supply has no ID,
     * or updates the existing document otherwise.
     *
     * @param customSupply the custom supply to save
     * @return the saved custom supply, including any auto-generated ID
     */
    CustomSupply save(CustomSupply customSupply);

    /**
     * Removes a custom supply document from the store by its unique identifier.
     *
     * @param id the unique identifier of the custom supply to delete
     */
    void deleteById(String id);
}
