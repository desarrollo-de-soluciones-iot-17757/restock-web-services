package com.uitopic.restock.platform.iam.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

/**
 * Query to get a user by their account ID.
 *
 * @param accountId the account ID of the user to retrieve
 */
public record GetUserByAccountIdQuery(AccountId accountId) {
}
