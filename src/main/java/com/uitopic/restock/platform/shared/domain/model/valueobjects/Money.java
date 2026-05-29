package com.uitopic.restock.platform.shared.domain.model.valueobjects;

import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a monetary value with an amount and a currency code.
 *
 * @param amount the monetary amount, which must be non-negative
 * @param currencyCode the currency code (e.g., "USD", "EUR", "GBP", "PEN", "CNY"), which must be supported
 */
public record Money(
    BigDecimal amount,
    String currencyCode
) {
    private static final List<String> SUPPORTED_CURRENCIES = List.of("USD", "EUR", "GBP", "PEN", "CNY");

    /**
     * Constructs a new Money instance with the specified amount and currency code.
     *
     * @param amount the monetary amount, which must be non-negative
     * @param currencyCode the currency code (e.g., "USD", "EUR", "GBP", "PEN", "CNY"), which must be supported
     * @throws IllegalArgumentException if the amount is negative or if the currency code is not supported
     */
    public Money {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        if (!isCurrencyCodeValid(currencyCode)) {
            throw new IllegalArgumentException("Unsupported currency: " + currencyCode);
        }
    }

    // Validates that the currency code is one of the supported currencies
    private boolean isCurrencyCodeValid(String code) {
        return SUPPORTED_CURRENCIES.contains(code);
    }

    // Validates that the amount is non-negative and not null
    private boolean isAmountValid(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Returns the amount of this Money instance.
     *
     * @return the amount as a BigDecimal
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Returns the currency code of this Money instance.
     *
     * @return the currency code (e.g., "USD", "EUR", "GBP", "PEN", "CNY")
     */
    public String currencyCode() {
        return currencyCode;
    }

    /**
     * Returns a string representation of this Money instance, including the amount and currency code.
     *
     * @return a string in the format "amount currencyCode" (e.g., "100.00 USD")
     */
    public @NonNull String toString() {
        return String.format("%s %s", amount, currencyCode);
    }

    /**
     * Adds another Money to this one, ensuring they have the same currency.
     *
     * @param other the Money to add
     * @return a new Money instance representing the sum
     */
    public Money add(Money other) {
        if (!this.currencyCode.equals(other.currencyCode)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currencyCode);
    }

    /**
     * Subtracts another Money from this one, ensuring the result is not negative.
     *
     * @param other the Money to subtract
     * @return a new Money instance representing the difference
     */
    public Money subtract(Money other) {
        if (!this.currencyCode.equals(other.currencyCode)) {
            throw new IllegalArgumentException("Cannot subtract money with different currencies");
        }

        if (this.amount.compareTo(other.amount) < 0) {
            throw new IllegalArgumentException("Resulting amount cannot be negative");
        }

        return new Money(this.amount.subtract(other.amount), this.currencyCode);
    }
}
