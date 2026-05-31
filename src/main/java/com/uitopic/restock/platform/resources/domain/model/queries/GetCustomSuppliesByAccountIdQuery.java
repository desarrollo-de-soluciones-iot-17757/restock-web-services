package com.uitopic.restock.platform.resources.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

/**
 * Query to get all the custom supplies from a specific account within the resources bounded context.
 *
 * @param accountId the ID of the account to retrieve custom supplies for
 */
public record GetCustomSuppliesByAccountIdQuery(AccountId accountId) {

}
