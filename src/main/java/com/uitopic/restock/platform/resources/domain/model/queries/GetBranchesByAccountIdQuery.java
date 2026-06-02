package com.uitopic.restock.platform.resources.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

/**
 * Query to get all branches from an account.
 */
public record GetBranchesByAccountIdQuery(
        AccountId accountId
) {
    public GetBranchesByAccountIdQuery {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
    }
}