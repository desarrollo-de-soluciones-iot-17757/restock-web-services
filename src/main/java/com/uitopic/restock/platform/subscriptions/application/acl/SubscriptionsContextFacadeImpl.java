package com.uitopic.restock.platform.subscriptions.application.acl;

import com.uitopic.restock.platform.subscriptions.domain.model.commands.CreateAccountCommand;
import com.uitopic.restock.platform.subscriptions.domain.services.AccountCommandService;
import com.uitopic.restock.platform.subscriptions.interfaces.acl.SubscriptionsContextFacade;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionsContextFacadeImpl implements SubscriptionsContextFacade {

    private final AccountCommandService accountCommandService;

    public SubscriptionsContextFacadeImpl(AccountCommandService accountCommandService) {
        this.accountCommandService = accountCommandService;
    }

    @Override
    public String createAccount(String accountId, String email) {
        var command = new CreateAccountCommand(accountId, email);
        var account = accountCommandService.handle(command);
        return account.getId();
    }
}
