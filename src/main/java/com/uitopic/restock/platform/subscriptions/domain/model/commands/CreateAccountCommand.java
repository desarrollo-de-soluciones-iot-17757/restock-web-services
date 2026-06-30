package com.uitopic.restock.platform.subscriptions.domain.model.commands;

public record CreateAccountCommand(
        String accountId,
        String email
) {
}
