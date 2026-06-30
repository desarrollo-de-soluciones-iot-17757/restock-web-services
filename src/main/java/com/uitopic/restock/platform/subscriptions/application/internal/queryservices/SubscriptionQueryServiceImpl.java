package com.uitopic.restock.platform.subscriptions.application.internal.queryservices;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import com.uitopic.restock.platform.subscriptions.domain.model.queries.GetSubscriptionByAccountIdQuery;
import com.uitopic.restock.platform.subscriptions.domain.repositories.SubscriptionRepository;
import com.uitopic.restock.platform.subscriptions.domain.services.SubscriptionQueryService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionByAccountIdQuery query) {
        return subscriptionRepository.findByAccountId(query.accountId());
    }
}
