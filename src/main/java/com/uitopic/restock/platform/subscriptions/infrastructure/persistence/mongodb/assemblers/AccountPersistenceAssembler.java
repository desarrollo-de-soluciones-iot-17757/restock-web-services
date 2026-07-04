package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Account;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.AccountPersistenceEntity;

public final class AccountPersistenceAssembler {

    private AccountPersistenceAssembler() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static Account toDomainFromPersistence(AccountPersistenceEntity entity) {
        if (entity == null) return null;
        var account = new Account();
        account.setId(entity.getId());
        account.setAccountId(entity.getAccountId());
        account.setEmail(entity.getEmail());
        account.setStripeCustomerId(entity.getStripeCustomerId());
        account.setStatus(entity.getStatus());
        account.setCurrentPlanId(entity.getCurrentPlanId());
        return account;
    }

    public static AccountPersistenceEntity toPersistenceFromDomain(Account account) {
        if (account == null) return null;
        var entity = new AccountPersistenceEntity();
        if (account.getId() != null) {
            entity.setId(account.getId());
        }
        entity.setAccountId(account.getAccountId());
        entity.setEmail(account.getEmail());
        entity.setStripeCustomerId(account.getStripeCustomerId());
        entity.setStatus(account.getStatus());
        entity.setCurrentPlanId(account.getCurrentPlanId());
        return entity;
    }
}
