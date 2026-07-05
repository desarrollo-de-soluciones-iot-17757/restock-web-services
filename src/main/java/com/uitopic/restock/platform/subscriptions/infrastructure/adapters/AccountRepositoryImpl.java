package com.uitopic.restock.platform.subscriptions.infrastructure.adapters;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Account;
import com.uitopic.restock.platform.subscriptions.domain.repositories.AccountRepository;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.assemblers.AccountPersistenceAssembler;
import com.uitopic.restock.platform.subscriptions.infrastructure.persistence.mongodb.repositories.AccountPersistenceRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountPersistenceRepository accountPersistenceRepository;

    public AccountRepositoryImpl(AccountPersistenceRepository accountPersistenceRepository) {
        this.accountPersistenceRepository = accountPersistenceRepository;
    }

    @Override
    public Optional<Account> findById(String id) {
        return accountPersistenceRepository.findById(id)
                .map(AccountPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Account> findByAccountId(AccountId accountId) {
        return accountPersistenceRepository.findByAccountId(accountId)
                .map(AccountPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Account> findByStripeCustomerId(String stripeCustomerId) {
        return accountPersistenceRepository.findByStripeCustomerId(stripeCustomerId)
                .map(AccountPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Account save(Account account) {
        var entity = AccountPersistenceAssembler.toPersistenceFromDomain(account);
        var saved = accountPersistenceRepository.save(entity);
        return AccountPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteById(String id) {
        accountPersistenceRepository.deleteById(id);
    }
}
