package com.uitopic.restock.platform.subscriptions.application.internal.commandservices;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Account;
import com.uitopic.restock.platform.subscriptions.domain.model.commands.CreateAccountCommand;
import com.uitopic.restock.platform.subscriptions.domain.repositories.AccountRepository;
import com.uitopic.restock.platform.subscriptions.domain.services.AccountCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountCommandServiceImpl implements AccountCommandService {

    private final AccountRepository accountRepository;

    public AccountCommandServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account handle(CreateAccountCommand command) {
        AccountId accountId = new AccountId(command.accountId());
        var existing = accountRepository.findByAccountId(accountId);
        if (existing.isPresent()) {
            log.info("Account already exists for accountId: {}", command.accountId());
            return existing.get();
        }

        Account account = Account.builder()
                .accountId(accountId)
                .email(command.email())
                .stripeCustomerId(null)
                .currentPlanId(null)
                .build();

        Account saved = accountRepository.save(account);
        log.info("Subscription account created: id={}, email={}", saved.getId(), saved.getEmail());
        return saved;
    }
}
