package com.uitopic.restock.platform.iam.domain.model.valueobjects;

/**
 * Represents an Email address value object.
 * Validates the email string format to ensure correctness.
 */
public record Email(String email) {

    /**
     * Constructs and validates the Email value object.
     * Checks that the email string matches a standard email regular expression pattern.
     *
     * @param email the raw email address string to validate
     * @throws IllegalArgumentException if the email format is invalid or null
     */
    public Email {
        if (email == null || !email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$"))
            throw new IllegalArgumentException("Invalid email format: " + email);
    }

    /**
     * Gets the raw email address string.
     *
     * @return the raw email address string
     */
    public String getEmail() {
        return email;
    }
}
