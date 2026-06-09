package com.uitopic.restock.platform.shared.domain.model.valueobjects;

/**
 * Value object representing an account ID.
 *
 * @param accountId the unique identifier for the account
 */
public record AccountId(
        String accountId
) {

    // Constructor validation
    public AccountId {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or blank");
        }
    }

    /**
     * Returns the account ID.
     *
     * @return the account ID
     */
    public String getAccountId() {
        return accountId;
    }
}
