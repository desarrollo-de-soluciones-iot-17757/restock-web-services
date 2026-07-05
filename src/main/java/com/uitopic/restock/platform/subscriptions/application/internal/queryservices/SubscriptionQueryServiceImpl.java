package com.uitopic.restock.platform.subscriptions.application.internal.queryservices;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import com.uitopic.restock.platform.subscriptions.domain.model.queries.GetSubscriptionByAccountIdQuery;
import com.uitopic.restock.platform.subscriptions.domain.repositories.SubscriptionRepository;
import com.uitopic.restock.platform.subscriptions.domain.services.SubscriptionQueryService;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Account;
import com.uitopic.restock.platform.subscriptions.domain.repositories.AccountRepository;
import com.uitopic.restock.platform.subscriptions.interfaces.rest.resources.InvoiceResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;
    private final AccountRepository accountRepository;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository,
                                       AccountRepository accountRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionByAccountIdQuery query) {
        return subscriptionRepository.findByAccountId(query.accountId());
    }

    @Override
    public List<InvoiceResource> getInvoicesByAccountId(String accountIdStr) {
        AccountId accountId = new AccountId(accountIdStr);
        Account account = accountRepository.findByAccountId(accountId).orElse(null);
        if (account == null || account.getStripeCustomerId() == null || account.getStripeCustomerId().isBlank()) {
            return Collections.emptyList();
        }

        try {
            com.stripe.param.InvoiceListParams params = com.stripe.param.InvoiceListParams.builder()
                    .setCustomer(account.getStripeCustomerId())
                    .setLimit(20L)
                    .build();

            com.stripe.model.InvoiceCollection invoices = com.stripe.model.Invoice.list(params);
            List<InvoiceResource> resources = new ArrayList<>();
            for (com.stripe.model.Invoice invoice : invoices.getData()) {
                resources.add(new InvoiceResource(
                        invoice.getId(),
                        invoice.getNumber(),
                        invoice.getAmountPaid() / 100.0,
                        invoice.getInvoicePdf(),
                        invoice.getStatus(),
                        invoice.getCreated() * 1000L
                ));
            }
            return resources;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch invoices from Stripe: " + e.getMessage(), e);
        }
    }
}
