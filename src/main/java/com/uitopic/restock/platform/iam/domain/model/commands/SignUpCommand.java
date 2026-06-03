package com.uitopic.restock.platform.iam.domain.model.commands;

/**
 * This record represents the command to sign up a new user in the system. It contains all the necessary information required for the registration process, such as business name, email, password, role, phone number, and country.
 *
 * @param businessName the name of the business associated with the user
 * @param email the email address of the user
 * @param password the password for the user's account
 * @param role the role assigned to the user (e.g., admin, user, etc.)
 * @param phone the phone number of the user
 * @param country the country where the user is located
 */
public record SignUpCommand(String businessName, String email, String password, String role, String phone, String country) {
}
