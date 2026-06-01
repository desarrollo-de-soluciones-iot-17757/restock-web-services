package com.uitopic.restock.platform.shared.domain.model.valueobjects;

/**
 * Value object representing an address.
 *
 * @param address       the street address
 * @param city          the city of the address
 * @param regionOrState the region or state of the address
 * @param country       the country of the address
 */
public record Address(
        String address,
        String city,
        String regionOrState,
        String country
) {

    /**
     * Validation logic in the constructor to ensure that all fields are provided and not blank.
     * This ensures that an Address instance is always in a valid state.
     */
    public Address {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or blank");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("City cannot be null or blank");
        }
        if (regionOrState == null || regionOrState.isBlank()) {
            throw new IllegalArgumentException("Region or state cannot be null or blank");
        }
        if (country == null || country.isBlank()) {
            throw new IllegalArgumentException("Country cannot be null or blank");
        }
    }

    /**
     * Returns the full address as a formatted string.
     *
     * @return the full address in the format "address, city, regionOrState, country"
     */
    public String getFullAddress() {
        return String.format("%s, %s, %s, %s", address, city, regionOrState, country);
    }
}
