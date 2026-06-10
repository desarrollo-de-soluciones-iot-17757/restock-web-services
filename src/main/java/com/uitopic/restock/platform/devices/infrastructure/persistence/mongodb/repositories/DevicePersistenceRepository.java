package com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.repositories;

import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import com.uitopic.restock.platform.devices.infrastructure.persistence.mongodb.entities.DevicePersistenceEntity;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DevicePersistenceRepository is a Spring Data MongoDB repository interface for managing DevicePersistenceEntity objects in the MongoDB database. It provides methods for finding devices by their MAC address, retrieving all devices associated with a specific account ID, and checking for the existence of devices based on their MAC address or ID.
 */
@Repository
public interface DevicePersistenceRepository extends MongoRepository<DevicePersistenceEntity, String> {

    /**
     * Finds a device by its MAC address.
     *
     * @param macAddress the MAC address of the device to find
     * @return an Optional containing the DevicePersistenceEntity if found, or an empty Optional if no device with the given MAC address exists
     */
    Optional<DevicePersistenceEntity> findByMacAddress(MacAddress macAddress);

    /**
     * Finds all devices associated with a specific account ID.
     *
     * @param accountId the unique identifier of the account for which to retrieve devices
     * @return a list of DevicePersistenceEntity objects associated with the given account ID
     */
    List<DevicePersistenceEntity> findAllByAccountId(AccountId accountId);

    /**
     * Checks if a device with the given MAC address exists in the repository.
     *
     * @param macAddress the MAC address of the device to check for existence
     * @return true if a device with the given MAC address exists, false otherwise
     */
    boolean existsByMacAddress(MacAddress macAddress);

    /**
     * Checks if a device with the given ID exists in the repository.
     *
     * @param id the unique identifier of the device to check for existence
     * @return true if a device with the given ID exists, false otherwise
     */
    boolean existsById(String id);
}
