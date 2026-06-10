package com.uitopic.restock.platform.shared.interfaces.rest.transform;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * The SharedValueObjectFromStringAssembler class provides static methods to convert string representations of value objects into their corresponding domain model value objects.
 * This class is used to facilitate the transformation of string inputs from REST API requests into the appropriate value objects used within the domain model.
 */
public class SharedValueObjectFromStringAssembler {

    /**
     * Converts a string representation of an account ID into an AccountId value object.
     *
     * @param accountId the string representation of the account ID
     * @return an AccountId value object constructed from the provided string
     */
    public static AccountId toAccountIdFromString(@NotBlank String accountId) {
        return new AccountId(accountId);
    }

    /**
     * Converts a string representation of a batch ID into a BatchId value object.
     *
     * @param batchId the string representation of the batch ID
     * @return a BatchId value object constructed from the provided string
     */
    public static BatchId toBatchIdFromString(@NotBlank String batchId) {
        return new BatchId(batchId);
    }

    /**
     * Converts a string representation of a device ID into a DeviceId value object.
     *
     * @param deviceId the string representation of the device ID
     * @return a DeviceId value object constructed from the provided string
     */
    public static DeviceId toDeviceIdFromString(@NotBlank String deviceId) {
        return new DeviceId(deviceId);
    }

    /**
     * Converts a string representation of a unit measurement into a UnitMeasurement value object.
     *
     * @param unitMeasurement the string representation of the unit measurement (e.g., "pieces", "boxes", "liters", etc.)
     * @return a UnitMeasurement value object constructed from the provided string
     */
    public static UnitMeasurement toUnitMeasurementFromString(@NotBlank String unitMeasurement) {
        return new UnitMeasurement(unitMeasurement);
    }

    /**
     * Converts a string representation of an image URL into an ImageURL value object.
     *
     * @param imageURL the string representation of the image URL
     * @return an ImageURL value object constructed from the provided string
     */
    public static ImageURL toImageURLFromString(@NotBlank String imageURL, String accountId) {
        return new ImageURL(imageURL, accountId);
    }

    /**
     * Converts a string representation of an amount and a currency into a Money value object.
     *
     * @param money the string representation of the amount and currency, expected in the format "amount currency" (e.g., "100.00 USD")
     * @return a Money value object constructed from the provided amount and currency
     */
    public static Money toMoneyFromString(@NotBlank String money) {

        // Split the input string into parts (amount and currency)
        var parts = money.split(" ");

        // Validate that the input string has exactly two parts: amount and currency
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid money format. Expected format: 'amount currency' (e.g., '100.00 USD')");
        }

        // Parse the amount part into a BigDecimal, handling empty strings as zero
        BigDecimal amount = BigDecimal.valueOf(parts[0].isEmpty() ? 0 : Double.parseDouble(parts[0]));

        // Extract the currency part
        String currency = parts[1];

        // Return a new Money value object constructed from the parsed amount and currency
        return new Money(amount, currency);
    }
}
