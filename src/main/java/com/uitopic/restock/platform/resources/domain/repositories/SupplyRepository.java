package com.uitopic.restock.platform.resources.domain.repositories;

import com.uitopic.restock.platform.resources.domain.model.entities.Supply;

import java.util.List;
import java.util.Optional;

public interface SupplyRepository {
    List<Supply> findAll();
    Optional<Supply> findById(String id);
    Supply save(Supply supply);
    void deleteById(String id);
}
