package com.uitopic.restock.platform.subscriptions.infrastructure.adapters;

import com.uitopic.restock.platform.subscriptions.domain.model.entities.Plan;
import com.uitopic.restock.platform.subscriptions.domain.repositories.PlanRepository;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers.PlanPersistenceAssembler;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.repositories.PlanPersistenceRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class PlanRepositoryImpl implements PlanRepository {

    private final PlanPersistenceRepository planPersistenceRepository;

    public PlanRepositoryImpl(PlanPersistenceRepository planPersistenceRepository) {
        this.planPersistenceRepository = planPersistenceRepository;
    }

    @Override
    public Optional<Plan> findById(String id) {
        return planPersistenceRepository.findById(id)
                .map(PlanPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Plan> findByStripePriceId(String stripePriceId) {
        return planPersistenceRepository.findByStripePriceId(stripePriceId)
                .map(PlanPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Plan> findAll() {
        return planPersistenceRepository.findAll().stream()
                .map(PlanPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Plan save(Plan plan) {
        var entity = PlanPersistenceAssembler.toPersistenceFromDomain(plan);
        var saved = planPersistenceRepository.save(entity);
        return PlanPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteAll() {
        planPersistenceRepository.deleteAll();
    }
}
