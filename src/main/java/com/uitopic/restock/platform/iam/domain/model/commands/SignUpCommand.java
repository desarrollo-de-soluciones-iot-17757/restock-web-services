package com.uitopic.restock.platform.iam.domain.model.commands;

public record SignUpCommand(String businessName, String email, String password, String role) {
}
