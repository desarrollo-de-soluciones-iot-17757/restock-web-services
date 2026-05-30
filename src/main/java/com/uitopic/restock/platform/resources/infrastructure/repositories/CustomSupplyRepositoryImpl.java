package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.CustomSupplyMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomSupplyRepositoryImpl implements CustomSupplyRepository {

    private final CustomSupplyMongoRepository customSupplyMongoRepository;

    public CustomSupplyRepositoryImpl(CustomSupplyMongoRepository customSupplyMongoRepository) {
        this.customSupplyMongoRepository = customSupplyMongoRepository;
    }

    @Override public List<CustomSupply> findByAccountId(AccountId accountId) { return customSupplyMongoRepository.findByAccountId(accountId); }
    @Override public Optional<CustomSupply> findById(String id) { return customSupplyMongoRepository.findById(id); }
    @Override public Boolean existsByAccountIdAndName(AccountId accountId, String name) { return customSupplyMongoRepository.existsByAccountIdAndName(accountId, name); }
    @Override public CustomSupply save(CustomSupply cs) { return customSupplyMongoRepository.save(cs); }
    @Override public void deleteById(String id) { customSupplyMongoRepository.deleteById(id); }
}
