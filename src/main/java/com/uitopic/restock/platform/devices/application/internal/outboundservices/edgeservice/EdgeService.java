package com.uitopic.restock.platform.devices.application.internal.outboundservices.edgeservice;

/**
 *  This interface defines the contract for the EdgeService, which is responsible for handling operations related to edge devices in the system. The EdgeService provides a method to register a device using its MAC address. This allows the system to keep track of connected edge devices and manage them accordingly. The implementation of this interface will handle the actual logic for registering devices and maintaining their information within the system.
 */
public interface EdgeService {


    void registerDevice(String macAddress, Double minStock, Double maxStock, Double minPercentage, Double maxPercentage,Double minCelsius, Double maxCelsius);
}
