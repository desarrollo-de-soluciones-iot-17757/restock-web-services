package com.uitopic.restock.platform.tracking.domain.repositories;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.PhysicalAnomaly;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for PhysicalAnomaly aggregates.
 */
public interface PhysicalAnomalyRepository {
    PhysicalAnomaly save(PhysicalAnomaly physicalAnomaly);
    Optional<PhysicalAnomaly> findById(String id);
    List<PhysicalAnomaly> findByDeviceId(String deviceId);
}
