package com.uitopic.restock.platform.profiles.infrastructure.adapters;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Business;
import com.uitopic.restock.platform.profiles.domain.repositories.BusinessRepository;
import com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.assemblers.BusinessPersistenceAssembler;
import com.uitopic.restock.platform.profiles.infrastructure.persistence.mongodb.repositories.BusinessPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BusinessRepositoryImpl implements BusinessRepository {

    private final BusinessPersistenceRepository businessPersistenceRepository;

    public BusinessRepositoryImpl(BusinessPersistenceRepository businessPersistenceRepository) {
        this.businessPersistenceRepository = businessPersistenceRepository;
    }

    @Override
    public List<Business> findAll() {
        return businessPersistenceRepository.findAll().stream()
                .map(BusinessPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Business> findById(String id) {
        return businessPersistenceRepository.findById(id)
                .map(BusinessPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Business> findByUserId(String userId) {
        return businessPersistenceRepository.findByUserId(userId).stream()
                .map(BusinessPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Business save(Business business) {
        var saved = businessPersistenceRepository.save(BusinessPersistenceAssembler.toPersistenceFromDomain(business));
        return BusinessPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteById(String id) {
        businessPersistenceRepository.deleteById(id);
    }
}
