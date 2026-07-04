package com.uitopic.restock.platform.subscriptions.domain.repositories;

import com.uitopic.restock.platform.subscriptions.domain.model.entities.Plan;
import java.util.List;
import java.util.Optional;

public interface PlanRepository {
    Optional<Plan> findById(String id);
    Optional<Plan> findByStripePriceId(String stripePriceId);
    List<Plan> findAll();
    Plan save(Plan plan);
    void deleteAll();
}
