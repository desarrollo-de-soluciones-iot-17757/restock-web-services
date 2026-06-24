package com.uitopic.restock.platform.subscriptions.infrastructure.adapters;

import com.uitopic.restock.platform.subscriptions.domain.model.entities.SubscriptionPlan;
import com.uitopic.restock.platform.subscriptions.domain.repositories.PlanRepository;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.PlanType;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers.PlanPersistenceAssembler;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.repositories.PlanPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PlanRepositoryImpl implements PlanRepository {
    private final PlanPersistenceRepository repository;

    public PlanRepositoryImpl(PlanPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public SubscriptionPlan save(SubscriptionPlan plan) {
        return PlanPersistenceAssembler.toDomainFromPersistence(
                repository.save(PlanPersistenceAssembler.toPersistenceFromDomain(plan))
        );
    }

    @Override
    public Optional<SubscriptionPlan> findByType(PlanType type) {
        return repository.findByType(type).map(PlanPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<SubscriptionPlan> findAllActive() {
        return repository.findAll().stream()
                .map(PlanPersistenceAssembler::toDomainFromPersistence)
                .filter(SubscriptionPlan::active)
                .toList();
    }
}
