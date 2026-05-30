package com.uitopic.restock.platform.iam.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

/**
 * Query to retrieve all users associated with a given account ID.
 * Designed to support multiple worker users per account in the future.
 *
 * @param accountId the account ID whose users should be retrieved
 */
public record GetAllUsersByAccountIdQuery(AccountId accountId) {
}
