package com.uitopic.restock.platform.subscriptions.domain.services;

import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Account;
import com.uitopic.restock.platform.subscriptions.domain.model.commands.CreateAccountCommand;

public interface AccountCommandService {
    Account handle(CreateAccountCommand command);
}
