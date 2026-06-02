package com.uitopic.restock.platform.resources.interfaces.rest.transform;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.StockRange;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.ImageURL;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;

import java.math.BigDecimal;

/**
 * Assembler to convert REST resource values into domain value objects.
 */
public class ResourcesValueObjectFromStringAssembler {

    private ResourcesValueObjectFromStringAssembler() {
    }

    /**
     * Converts minimum and maximum stock values into a StockRange value object.
     *
     * @param minimumStock minimum stock value
     * @param maximumStock maximum stock value
     * @return stock range value object
     */
    public static StockRange toStockRangeFromValues(Double minimumStock, Double maximumStock) {
        return new StockRange(minimumStock, maximumStock);
    }

    /**
     * Converts a string representation of money into a Money value object.
     *
     * Expected format: "10.00 PEN".
     *
     * @param moneyStr money string
     * @return money value object
     */
    public static Money toMoneyFromString(String moneyStr) {
        if (moneyStr == null || moneyStr.isBlank()) {
            throw new IllegalArgumentException("Money value cannot be null or blank");
        }

        String[] parts = moneyStr.trim().split("\\s+");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Money value must have format: 'amount currency', e.g. '10.00 PEN'");
        }

        try {
            BigDecimal amount = new BigDecimal(parts[0]);
            String currencyCode = parts[1].toUpperCase();

            return new Money(amount, currencyCode);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid money amount: " + parts[0], e);
        }
    }

    /**
     * Converts a string into a UnitMeasurement value object.
     *
     * @param unitMeasurement unit measurement string
     * @return unit measurement value object
     */
    public static UnitMeasurement toUnitMeasurementFromString(String unitMeasurement) {
        if (unitMeasurement == null || unitMeasurement.isBlank()) {
            throw new IllegalArgumentException("Unit measurement cannot be null or blank");
        }

        return new UnitMeasurement(unitMeasurement);
    }

    /**
     * Converts image URL data into an ImageURL value object.
     *
     * @param imageUrl image URL
     * @param imagePublicId Cloudinary public ID
     * @return image URL value object, or null if no image URL was provided
     */
    public static ImageURL toImageURLFromStrings(String imageUrl, String imagePublicId) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }

        return new ImageURL(imageUrl, imagePublicId);
    }
}