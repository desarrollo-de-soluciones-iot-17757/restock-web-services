package com.uitopic.restock.platform.resources.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

/** Query to retrieve all branches for a given account within the resources bounded context. */
public record GetBranchesByAccountIdQuery(AccountId accountId) {}
