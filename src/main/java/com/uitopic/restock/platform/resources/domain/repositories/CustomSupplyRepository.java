package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

public interface CustomSupplyRepository {
    List<CustomSupply> findByAccountId(AccountId accountId);
    Optional<CustomSupply> findById(String id);
    Boolean existsByAccountIdAndName(AccountId accountId, String name);
    CustomSupply save(CustomSupply customSupply);
    void deleteById(String id);
}
