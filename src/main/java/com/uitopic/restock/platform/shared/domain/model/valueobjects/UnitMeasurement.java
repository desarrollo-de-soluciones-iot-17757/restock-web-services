package com.uitopic.restock.platform.shared.domain.model.valueobjects;

/**
 * Value object representing a unit of measurement for supplies.
 * It encapsulates the name of the unit and provides methods to access it.
 *
 * @param unitName the name of the unit measurement (e.g., "kilogram", "liter", "piece", "bottles")
 */
public record UnitMeasurement(
        String unitName
) {

    // Constructor validation to ensure that unitName is not null or blank
    public UnitMeasurement {
        if (unitName == null || unitName.isBlank()) {
            throw new IllegalArgumentException("Unit name cannot be null or blank");
        }
    }

    /**
     * Factory method to create a new UnitMeasurement instance with the given unit name.
     *
     * @param unitName the name of the unit measurement
     * @return a new UnitMeasurement instance with the given unit name
     */
    public static UnitMeasurement of(String unitName) {
        return new UnitMeasurement(unitName);
    }

    /**
     * Returns the name of the unit measurement.
     *
     * @return the name of the unit measurement
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Returns the abbreviation of the unit measurement, which is the first three letters of the unit name in uppercase.
     *
     * @return the abbreviation of the unit measurement
     */
    public String getAbbreviation() {
        return unitName.substring(0, 3).toUpperCase();
    }
}
