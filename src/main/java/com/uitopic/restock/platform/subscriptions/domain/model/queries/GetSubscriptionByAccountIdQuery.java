package com.uitopic.restock.platform.subscriptions.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

public record GetSubscriptionByAccountIdQuery(
        AccountId accountId
) {
}
