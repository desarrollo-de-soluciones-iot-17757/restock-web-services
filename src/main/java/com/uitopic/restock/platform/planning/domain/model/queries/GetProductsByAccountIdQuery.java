package com.uitopic.restock.platform.planning.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

/**
 * Query to retrieve all {@link com.uitopic.restock.platform.planning.domain.model.aggregates.Product}
 * aggregates belonging to a specific tenant account.
 *
 * @param accountId the account whose products are to be retrieved
 */
public record GetProductsByAccountIdQuery(AccountId accountId) {}
