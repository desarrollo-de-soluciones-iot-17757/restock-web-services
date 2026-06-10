package com.uitopic.restock.platform.devices.domain.repositories;

import com.uitopic.restock.platform.devices.domain.model.entities.DeviceThreshold;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing DeviceThreshold entities.
 */
public interface DeviceThresholdRepository {

    /**
     * Saves a DeviceThreshold entity to the repository.
     *
     * @param threshold The DeviceThreshold entity to be saved.
     * @return The saved DeviceThreshold entity.
     */
    DeviceThreshold save(DeviceThreshold threshold);

    /**
     * Finds a DeviceThreshold entity by its unique identifier.
     *
     * @param id The unique identifier of the DeviceThreshold entity.
     * @return An Optional containing the found DeviceThreshold entity, or empty if not found.
     */
    Optional<DeviceThreshold> findById(String id);

    /**
     * Finds a DeviceThreshold entity by the associated DeviceId.
     *
     * @param deviceId The DeviceId associated with the DeviceThreshold entity.
     * @return An Optional containing the found DeviceThreshold entity, or empty if not found.
     */
    Optional<DeviceThreshold> findByDeviceId(DeviceId deviceId);

    /**
     * Finds all DeviceThreshold entities associated with a specific AccountId.
     *
     * @param accountId The AccountId for which to find the DeviceThreshold entities.
     * @return A list of DeviceThreshold entities associated with the given AccountId.
     */
    List<DeviceThreshold> findAllByAccountId(AccountId accountId);

    /**
     * Deletes a DeviceThreshold entity by its unique identifier.
     *
     * @param id The unique identifier of the DeviceThreshold entity to be deleted.
     */
    void deleteById(String id);

    /**
     * Checks if a DeviceThreshold entity exists for a given DeviceId.
     *
     * @param deviceId The DeviceId to check for existence of a DeviceThreshold entity.
     * @return true if a DeviceThreshold entity exists for the given DeviceId, false otherwise.
     */
    Boolean existsByDeviceId(DeviceId deviceId);
}
