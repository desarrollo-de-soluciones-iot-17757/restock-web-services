package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;
import com.uitopic.restock.platform.resources.domain.repositories.SupplyRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.SupplyMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SupplyRepositoryImpl implements SupplyRepository {

    private final SupplyMongoRepository supplyMongoRepository;

    public SupplyRepositoryImpl(SupplyMongoRepository supplyMongoRepository) {
        this.supplyMongoRepository = supplyMongoRepository;
    }

    @Override public List<Supply> findAll() { return supplyMongoRepository.findAll(); }
    @Override public Optional<Supply> findById(String id) { return supplyMongoRepository.findById(id); }
    @Override public Supply save(Supply supply) { return supplyMongoRepository.save(supply); }
    @Override public void deleteById(String id) { supplyMongoRepository.deleteById(id); }
}
