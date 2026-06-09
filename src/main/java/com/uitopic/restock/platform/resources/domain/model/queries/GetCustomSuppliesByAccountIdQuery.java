package com.uitopic.restock.platform.resources.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

/**
 * Query to get all custom supplies from a specific account.
 *
 * @param accountId account identifier
 */
public record GetCustomSuppliesByAccountIdQuery(
        AccountId accountId
) {
    public GetCustomSuppliesByAccountIdQuery {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
    }
}