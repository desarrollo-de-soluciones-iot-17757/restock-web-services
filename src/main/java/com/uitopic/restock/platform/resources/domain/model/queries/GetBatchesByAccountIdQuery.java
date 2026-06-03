package com.uitopic.restock.platform.resources.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

/**
 * Query to get all batches from an account.
 */
public record GetBatchesByAccountIdQuery(
        AccountId accountId
) {
    public GetBatchesByAccountIdQuery {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
    }
}