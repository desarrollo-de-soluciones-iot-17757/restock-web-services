package com.uitopic.restock.platform.devices.application.internal.outboundservices.edgeservice;

/**
 *  This interface defines the contract for the EdgeService, which is responsible for handling operations related to edge devices in the system. The EdgeService provides a method to register a device using its MAC address. This allows the system to keep track of connected edge devices and manage them accordingly. The implementation of this interface will handle the actual logic for registering devices and maintaining their information within the system.
 */
public interface EdgeService {

    /**
     * Registers a device with the given MAC address. This method is responsible for adding the device to the system's registry, allowing it to be recognized and managed as part of the edge device network. The implementation of this method will handle the necessary logic to ensure that the device is properly registered and can communicate with the rest of the system.
     *
     * @param macAddress The MAC address of the device to be registered. This unique identifier is used to distinguish the device from others in the network and allows for proper management and communication within the system.
     */
    void registerDevice(String macAddress);
}
