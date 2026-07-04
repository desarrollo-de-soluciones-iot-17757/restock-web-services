package com.uitopic.restock.platform.subscriptions.domain.services;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Account;
import java.util.Optional;

public interface AccountQueryService {
    Optional<Account> findById(String id);
    Optional<Account> findByAccountId(AccountId accountId);
}
