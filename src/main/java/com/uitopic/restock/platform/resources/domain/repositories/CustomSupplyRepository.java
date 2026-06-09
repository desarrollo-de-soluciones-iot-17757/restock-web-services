package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing CustomSupply aggregates.
 */
public interface CustomSupplyRepository {

    /**
     * Saves a custom supply to the repository.
     *
     * @param customSupply the custom supply to save
     * @return the saved custom supply with any generated fields (e.g., id) populated
     */
    CustomSupply save(CustomSupply customSupply);

    /**
     * Finds all custom supplies that belong to an account.
     *
     * @param accountId account identifier
     * @return custom supplies owned by the account
     */
    List<CustomSupply> findByAccountId(AccountId accountId);

    /**
     * Finds a custom supply by its unique identifier.
     *
     * @param id custom supply identifier
     * @return an Optional containing the custom supply if found, or empty if not found
     */
    Optional<CustomSupply> findById(String id);

    /**
     * Finds a custom supply by its unique identifier.
     *
     * @return the custom supply with the given id, or null if not found
     */
    List<CustomSupply> findAll();

    /**
     * Checks whether a custom supply name already exists within an account.
     *
     * @param accountId account identifier
     * @param name custom supply name
     * @return true if the name already exists
     */
    boolean existsByAccountIdAndName(AccountId accountId, String name);

    /**
     * Checks whether another custom supply with the same name exists in an account.
     *
     * @param accountId account identifier
     * @param name custom supply name
     * @param id custom supply id to exclude
     * @return true if another custom supply with the same name exists
     */
    boolean existsByAccountIdAndNameAndIdNot(AccountId accountId, String name, String id);

    /**
     * Checks whether a custom supply already exists for an account and base supply.
     *
     * @param accountId account identifier
     * @param supplyId base supply identifier
     * @return true if the custom supply exists
     */
    boolean existsByAccountIdAndSupplyId(AccountId accountId, String supplyId);

    /**
     * Checks whether another custom supply already exists for an account and base supply.
     *
     * @param accountId account identifier
     * @param supplyId base supply identifier
     * @param id current custom supply identifier to exclude
     * @return true if another custom supply exists
     */
    boolean existsByAccountIdAndSupplyIdAndIdNot(AccountId accountId, String supplyId, String id);

    /**
     * Deletes a custom supply by its unique identifier.
     *
     * @param id custom supply identifier
     */
    void deleteById(String id);
}
