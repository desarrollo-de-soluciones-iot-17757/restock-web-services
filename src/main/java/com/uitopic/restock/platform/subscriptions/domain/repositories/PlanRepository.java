package com.uitopic.restock.platform.subscriptions.domain.repositories;

import com.uitopic.restock.platform.subscriptions.domain.model.entities.SubscriptionPlan;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.PlanType;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {
    SubscriptionPlan save(SubscriptionPlan plan);
    Optional<SubscriptionPlan> findByType(PlanType type);
    List<SubscriptionPlan> findAllActive();
}
