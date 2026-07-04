package com.uitopic.restock.platform.subscriptions.domain.services;

import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import com.uitopic.restock.platform.subscriptions.domain.model.queries.GetSubscriptionByAccountIdQuery;
import com.uitopic.restock.platform.subscriptions.interfaces.rest.resources.InvoiceResource;
import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryService {
    Optional<Subscription> handle(GetSubscriptionByAccountIdQuery query);
    List<InvoiceResource> getInvoicesByAccountId(String accountId);
}
