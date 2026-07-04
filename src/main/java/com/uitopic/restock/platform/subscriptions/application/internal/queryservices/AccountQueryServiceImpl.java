package com.uitopic.restock.platform.subscriptions.application.internal.queryservices;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Account;
import com.uitopic.restock.platform.subscriptions.domain.repositories.AccountRepository;
import com.uitopic.restock.platform.subscriptions.domain.services.AccountQueryService;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AccountQueryServiceImpl implements AccountQueryService {

    private final AccountRepository accountRepository;

    public AccountQueryServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> findById(String id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> findByAccountId(AccountId accountId) {
        return accountRepository.findByAccountId(accountId);
    }
}
