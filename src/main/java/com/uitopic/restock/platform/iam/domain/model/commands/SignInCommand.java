package com.uitopic.restock.platform.iam.domain.model.commands;

/**
 * Command for signing in a user.
 *
 * @param email the email of the user to sign in
 * @param password the password of the user to sign in
 */
public record SignInCommand(String email, String password) {
}
