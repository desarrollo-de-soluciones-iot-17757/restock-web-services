package com.uitopic.restock.platform.subscriptions.domain.repositories;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Account;
import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findById(String id);
    Optional<Account> findByAccountId(AccountId accountId);
    Optional<Account> findByStripeCustomerId(String stripeCustomerId);
    Account save(Account account);
    void deleteById(String id);
}
