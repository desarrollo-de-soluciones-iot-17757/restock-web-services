package com.uitopic.restock.platform.resources.domain.model.aggregates;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomSupplyTests {

    /**
     * Test case to verify that a CustomSupply object can be created successfully with valid parameters.
     */
    @Test
    void createCustomSupply_CorrectParameters_Success() {
        // Arrange
        var name = "Test Supply";
        var description = "This is a test supply.";
        var price = BigDecimal.valueOf(19.99);
        var currency = "PEN";

        var unitPrice = new Money(price, currency);

        // Act
        var result = CustomSupply.builder()
                .name(name)
                .description(description)
                .unitPrice(unitPrice)
                .build();

        // Assert
        assertNotNull(result);
    }

    /**
     * Test case to verify that creating a CustomSupply with an incorrect money value (e.g., negative price) throws an IllegalArgumentException.
     */
    @Test
    void createCustomSupply_IncorrectMoneyValue_ThrowsException() {
        // Arrange
        var name = "Test Supply";
        var description = "This is a test supply.";
        var price = BigDecimal.valueOf(-10.00); // Invalid negative price
        var currency = "PEN";

        // Act & Assert
        try {
            var unitPrice = new Money(price, currency);
            CustomSupply.builder()
                    .name(name)
                    .description(description)
                    .unitPrice(unitPrice)
                    .build();
        } catch (IllegalArgumentException e) {
            assertEquals("Amount must be non-negative", e.getMessage());
        }
    }

    /**
     * Test case to verify that creating a CustomSupply with a null name throws a NullPointerException, as the name is a required field.
     */
    @Test
    void createCustomSupply_NullName_ThrowsException() {
        // Arrange
        var description = "This is a test supply.";
        var price = BigDecimal.valueOf(19.99);
        var currency = "PEN";

        var unitPrice = new Money(price, currency);

        // Act & Assert
        try {
            CustomSupply.builder()
                    .name(null)
                    .description(description)
                    .unitPrice(unitPrice)
                    .build();
        } catch (NullPointerException e) {
            assertEquals("Name cannot be null", e.getMessage());
        }
    }

    /**
     * Test case to verify that creating a CustomSupply with a null unit measurement throws a NullPointerException, as the unit measurement is a required field.
     */
    @Test
    void createCustomSupply_NullMeasurementUnit_ThrowsException() {
        // Arrange
        var name = "Test Supply";
        var description = "This is a test supply.";
        var price = BigDecimal.valueOf(19.99);
        var currency = "PEN";

        var unitPrice = new Money(price, currency);

        // Act & Assert
        try {
            CustomSupply.builder()
                    .name(name)
                    .description(description)
                    .unitPrice(unitPrice)
                    .unitMeasurement(null) // Invalid null unit measurement
                    .build();
        } catch (NullPointerException e) {
            assertEquals("Unit measurement cannot be null", e.getMessage());
        }
    }

    /**
     * Test case to verify that updating a CustomSupply with valid parameters successfully updates the supply's details.
     */
    @Test
    void updateCustomSupply_ValidParameters_Success() {
        // Arrange
        var name = "Test Supply";
        var description = "This is a test supply.";
        var price = BigDecimal.valueOf(19.99);
        var currency = "PEN";

        var unitPrice = new Money(price, currency);

        var customSupply = CustomSupply.builder()
                .name(name)
                .description(description)
                .unitPrice(unitPrice)
                .build();

        // Act
        var newDescription = "Updated description.";
        var newPrice = BigDecimal.valueOf(29.99);
        var newUnitPrice = new Money(newPrice, currency);
        customSupply.update(newDescription, newUnitPrice, null, null, null);

        // Assert
        assertEquals(newDescription, customSupply.getDescription());
        assertEquals(newUnitPrice, customSupply.getUnitPrice());
    }

    /**
     * Test case to verify that updating a CustomSupply with some parameters (e.g., only description) leaves the other parameters (e.g., unit price) unchanged.
     */
    @Test
    void updateCustomSupply_WithSomeParameters_OthersRemainUnchanged() {
        // Arrange
        var name = "Test Supply";
        var description = "This is a test supply.";
        var price = BigDecimal.valueOf(19.99);
        var currency = "PEN";

        var unitPrice = new Money(price, currency);

        var customSupply = CustomSupply.builder()
                .name(name)
                .description(description)
                .unitPrice(unitPrice)
                .build();

        // Act
        var newDescription = "Updated description.";
        customSupply.update(newDescription, null, null, null, null);

        // Assert
        assertEquals(newDescription, customSupply.getDescription());
        assertEquals(unitPrice, customSupply.getUnitPrice()); // Unit price should remain unchanged
    }

    /**
     * Test case to verify that updating a CustomSupply with a null description does not change the existing description, ensuring that null values do not overwrite existing data.
     */
    @Test
    void updateCustomSupply_NullDescription_DescriptionRemainsUnchanged() {
        // Arrange
        var name = "Test Supply";
        var description = "This is a test supply.";
        var price = BigDecimal.valueOf(19.99);
        var currency = "PEN";

        var unitPrice = new Money(price, currency);

        var customSupply = CustomSupply.builder()
                .name(name)
                .description(description)
                .unitPrice(unitPrice)
                .build();

        // Act
        customSupply.update(null, null, null, null, null);

        // Assert
        assertEquals(description, customSupply.getDescription()); // Description should remain unchanged
    }
}
