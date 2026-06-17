package com.uitopic.restock.platform.planning.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

/**
 * Query to retrieve all products for a given account with their maximum
 * assemblable quantity based on available stock in the specified branch.
 *
 * @param accountId the tenant account whose products are to be evaluated
 * @param branchId  the branch in which to check ingredient stock levels
 */
public record GetProductsAvailabilityQuery(AccountId accountId, String branchId) {}
