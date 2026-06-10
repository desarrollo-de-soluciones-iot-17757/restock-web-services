package com.uitopic.restock.platform.devices.domain.repositories;

import com.uitopic.restock.platform.devices.domain.model.aggregates.Device;
import com.uitopic.restock.platform.devices.domain.model.valueobjects.MacAddress;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Device aggregates, providing methods for saving, retrieving, and deleting devices.
 */
public interface DeviceRepository {

    /**
     * Saves a device to the repository. If the device already exists, it will be updated; otherwise, a new device will be created.
     *
     * @param device the device to be saved or updated
     * @return the saved or updated device
     */
    Device save(Device device);

    /**
     * Finds a device by its unique identifier. Returns an Optional containing the device if found, or an empty Optional if not found.
     *
     * @param id the unique identifier of the device to be retrieved
     * @return an Optional containing the device if found, or an empty Optional if not found
     */
    Optional<Device> findById(String id);

    /**
     * Finds a device by its MAC address. Returns an Optional containing the device if found, or an empty Optional if not found.
     *
     * @param macAddress the MAC address of the device to be retrieved
     * @return an Optional containing the device if found, or an empty Optional if not found
     */
    Optional<Device> findByMacAddress(MacAddress macAddress);

    /**
     * Finds all devices associated with a specific account ID. Returns a list of devices belonging to the specified account.
     *
     * @param accountId the account ID for which to retrieve devices
     * @return a list of devices associated with the specified account ID
     */
    List<Device> findAllByAccountId(AccountId accountId);

    /**
     * Deletes a device from the repository by its unique identifier. If the device does not exist, no action is taken.
     *
     * @param id the unique identifier of the device to be deleted
     */
    void deleteById(String id);

    /**
     * Checks if a device with the specified MAC address already exists in the repository. Returns true if a device with the given MAC address exists, or false otherwise.
     *
     * @param macAddress the MAC address to check for existence
     * @return true if a device with the specified MAC address exists, or false otherwise
     */
    Boolean existsByMacAddress(MacAddress macAddress);

    /**
     * Checks if a device with the specified device ID already exists in the repository. Returns true if a device with the given device ID exists, or false otherwise.
     *
     * @param deviceId the device ID to check for existence
     * @return true if a device with the specified device ID exists, or false otherwise
     */
    Boolean existsByDeviceId(String deviceId);
}
