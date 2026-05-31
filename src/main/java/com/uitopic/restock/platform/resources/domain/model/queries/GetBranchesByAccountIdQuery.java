package com.uitopic.restock.platform.resources.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

/**
 * Query object for retrieving branches associated with a specific account ID.
 * @param accountId The ID of the account for which to retrieve branches.
 */
public record GetBranchesByAccountIdQuery(AccountId accountId) {}
