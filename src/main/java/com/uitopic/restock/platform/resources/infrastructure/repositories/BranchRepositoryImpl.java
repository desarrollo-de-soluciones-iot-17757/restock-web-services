package com.uitopic.restock.platform.resources.infrastructure.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.Branch;
import com.uitopic.restock.platform.resources.domain.repositories.BranchRepository;
import com.uitopic.restock.platform.resources.infrastructure.persistence.mongodb.repositories.BranchMongoRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BranchRepositoryImpl implements BranchRepository {

    private final BranchMongoRepository mongo;

    public BranchRepositoryImpl(BranchMongoRepository mongo) {
        this.mongo = mongo;
    }

    @Override public Branch save(Branch branch) { return mongo.save(branch); }
    @Override public Optional<Branch> findById(String id) { return mongo.findById(id); }
    @Override public List<Branch> findByAccountId(AccountId accountId) { return mongo.findByAccountId(accountId); }
    @Override public void deleteById(String id) { mongo.deleteById(id); }
    @Override public boolean existsByNameAndAccountId(String name, AccountId accountId) { return mongo.existsByNameAndAccountId(name, accountId); }
}
