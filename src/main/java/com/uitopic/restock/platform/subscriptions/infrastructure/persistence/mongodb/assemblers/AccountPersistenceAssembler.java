package com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers;

import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Account;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.entities.AccountPersistenceEntity;

public final class AccountPersistenceAssembler {

    private AccountPersistenceAssembler() {

    }

    public static Account toDomainFromPersistence(AccountPersistenceEntity entity) {
        return null;
    }

    public static AccountPersistenceEntity toPersistenceFromDomain(Account account) {
        return null;
    }
}
