package com.uitopic.restock.platform.subscriptions.domain.services;

import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import com.uitopic.restock.platform.subscriptions.domain.model.queries.GetSubscriptionByAccountIdQuery;
import java.util.Optional;

public interface SubscriptionQueryService {
    Optional<Subscription> handle(GetSubscriptionByAccountIdQuery query);
}
