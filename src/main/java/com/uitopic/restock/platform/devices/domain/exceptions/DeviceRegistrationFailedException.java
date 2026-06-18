package com.uitopic.restock.platform.devices.domain.exceptions;

/**
 *  Exception thrown when device registration fails due to various reasons such as invalid input, database errors, or other issues that prevent successful registration of a device. This exception can be used to provide meaningful error messages and handle specific scenarios related to device registration failures in the application.
 */
public class DeviceRegistrationFailedException extends RuntimeException {

    /**
     * Constructs a new DeviceRegistrationFailedException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the device registration failure
     */
    public DeviceRegistrationFailedException(String message) {
        super(message);
    }
}
