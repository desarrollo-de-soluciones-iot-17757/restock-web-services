package com.uitopic.restock.platform.shared.domain.model.valueobjects;

import java.util.Map;

/**
 * Value object representing a unit of measurement.
 *
 * The unit name is provided by the request, while the abbreviation is generated
 * by the backend using known measurement rules.
 *
 * @param unitName full unit name
 * @param abbreviation short unit abbreviation
 */
public record UnitMeasurement(
        String unitName,
        String abbreviation
) {

    private static final Map<String, String> ABBREVIATIONS = Map.ofEntries(
            Map.entry("unit", "un"),
            Map.entry("units", "un"),
            Map.entry("piece", "pc"),
            Map.entry("pieces", "pcs"),
            Map.entry("box", "box"),
            Map.entry("boxes", "box"),
            Map.entry("kilogram", "kg"),
            Map.entry("kilograms", "kg"),
            Map.entry("gram", "g"),
            Map.entry("grams", "g"),
            Map.entry("liter", "L"),
            Map.entry("liters", "L"),
            Map.entry("milliliter", "ml"),
            Map.entry("milliliters", "ml")
    );

    /**
     * Creates a unit measurement and generates its abbreviation.
     *
     * @param unitName full unit name
     */
    public UnitMeasurement(String unitName) {
        this(unitName, buildAbbreviation(unitName));
    }

    public UnitMeasurement {
        if (unitName == null || unitName.isBlank()) {
            throw new IllegalArgumentException("Unit name cannot be null or blank");
        }

        unitName = unitName.trim();

        if (abbreviation == null || abbreviation.isBlank()) {
            abbreviation = buildAbbreviation(unitName);
        }
    }

    private static String buildAbbreviation(String unitName) {
        if (unitName == null || unitName.isBlank()) {
            throw new IllegalArgumentException("Unit name cannot be null or blank");
        }

        String normalized = unitName.trim().toLowerCase();

        return ABBREVIATIONS.getOrDefault(
                normalized,
                normalized.length() <= 3
                        ? normalized
                        : normalized.substring(0, 3)
        );
    }
}