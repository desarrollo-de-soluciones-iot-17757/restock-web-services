package com.uitopic.restock.platform.profiles.domain.repositories;

import com.uitopic.restock.platform.profiles.domain.model.aggregates.Business;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository {
    List<Business> findAll();
    Optional<Business> findById(String id);
    List<Business> findByUserId(String userId);
    Business save(Business business);
    void deleteById(String id);
}
