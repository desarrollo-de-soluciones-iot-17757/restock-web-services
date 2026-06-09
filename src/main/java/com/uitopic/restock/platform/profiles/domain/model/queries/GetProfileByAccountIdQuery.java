package com.uitopic.restock.platform.profiles.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

/**
 * Query to get a profile by its account ID.
 *
 * @param accountId the account ID of the profile to retrieve
 */
public record GetProfileByAccountIdQuery(AccountId accountId) {
}
