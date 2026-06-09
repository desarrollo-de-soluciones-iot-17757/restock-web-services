package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DeviceThresholdPersistenceEntity;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DeviceId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing DeviceThresholdPersistenceEntity in MongoDB.
 */
@Repository
public interface DeviceThresholdPersistenceRepository extends MongoRepository<DeviceThresholdPersistenceEntity, String> {

    /**
     * Find all DeviceThresholdPersistenceEntity by accountId.
     *
     * @param accountId the account ID to filter by
     * @return a list of DeviceThresholdPersistenceEntity associated with the given account ID
     */
    List<DeviceThresholdPersistenceEntity> findAllByAccountId(AccountId accountId);

    /**
     * Find a DeviceThresholdPersistenceEntity by deviceId.
     *
     * @param deviceId the device ID to filter by
     * @return an Optional containing the DeviceThresholdPersistenceEntity if found, or empty if not found
     */
    Optional<DeviceThresholdPersistenceEntity> findByDeviceId(DeviceId deviceId);
}
