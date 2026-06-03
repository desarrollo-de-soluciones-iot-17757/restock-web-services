package com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MongoDB repository for CustomSupply documents.
 */
@Repository
public interface CustomSupplyRepository extends MongoRepository<CustomSupply, String> {

    /**
     * Finds all custom supplies that belong to an account.
     *
     * @param accountId account identifier
     * @return custom supplies owned by the account
     */
    List<CustomSupply> findByAccountId(AccountId accountId);

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
}