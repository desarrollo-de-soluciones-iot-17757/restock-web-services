package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;

/**
 * Repository non-framework interface for managing CustomSupply aggregates.
 * This interface defines the contract for data access operations related to CustomSupply aggregates, allowing for flexibility in implementation.
 */
public interface CustomSupplyRepository {

    /**
     * Finds a list of CustomSupply aggregates by the given account ID.
     *
     * @param accountId the account ID for which to fetch the custom supplies
     * @return a list of CustomSupply aggregates that are associated with the specified account ID
     */
    List<CustomSupply> findByAccountId(AccountId accountId);
}
