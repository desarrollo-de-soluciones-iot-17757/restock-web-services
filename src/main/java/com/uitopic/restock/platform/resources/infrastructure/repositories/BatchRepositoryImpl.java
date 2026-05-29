package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.BatchMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BatchRepositoryImpl implements BatchRepository {

    private final BatchMongoRepository mongo;

    public BatchRepositoryImpl(BatchMongoRepository mongo) {
        this.mongo = mongo;
    }

    @Override public Batch save(Batch batch) { return mongo.save(batch); }
    @Override public Optional<Batch> findById(String id) { return mongo.findById(id); }
    @Override public List<Batch> findAll() { return mongo.findAll(); }
    @Override public List<Batch> findByBranchId(String branchId) { return mongo.findByBranchId(branchId); }
    @Override public List<Batch> findByCustomSupplyId(String id) { return mongo.findByCustomSupplyId(id); }
    @Override public List<Batch> findByBranchIdAndCustomSupplyId(String branchId, String supplyId) { return mongo.findByBranchIdAndCustomSupplyId(branchId, supplyId); }
    @Override public List<Batch> findByAccountId(AccountId accountId) { return mongo.findByAccountId(accountId); }
    @Override public void deleteById(String id) { mongo.deleteById(id); }
}
