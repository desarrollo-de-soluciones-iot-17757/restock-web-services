package com.uitopic.restock.platform.subscriptions.application.internal.commandservices;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.subscriptions.application.internal.outboundservices.payments.PaymentService;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Account;
import com.uitopic.restock.platform.subscriptions.domain.model.aggregates.Subscription;
import com.uitopic.restock.platform.subscriptions.domain.model.commands.CreateSubscriptionCommand;
import com.uitopic.restock.platform.subscriptions.domain.model.entities.Plan;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.AccountStatus;
import com.uitopic.restock.platform.subscriptions.domain.model.valueobjects.SubscriptionStatus;
import com.uitopic.restock.platform.subscriptions.domain.repositories.AccountRepository;
import com.uitopic.restock.platform.subscriptions.domain.repositories.PlanRepository;
import com.uitopic.restock.platform.subscriptions.domain.repositories.SubscriptionRepository;
import com.uitopic.restock.platform.subscriptions.domain.services.SubscriptionCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;
    private final AccountRepository accountRepository;
    private final PlanRepository planRepository;
    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository,
                                           AccountRepository accountRepository,
                                           PlanRepository planRepository,
                                           PaymentService paymentService) {
        this.subscriptionRepository = subscriptionRepository;
        this.accountRepository = accountRepository;
        this.planRepository = planRepository;
        this.paymentService = paymentService;
    }

    @Override
    public Optional<Subscription> handle(CreateSubscriptionCommand command) {
        AccountId accountId = new AccountId(command.accountId());
        var plan = planRepository.findById(command.planId())
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with ID: " + command.planId()));

        var existingSub = subscriptionRepository.findByAccountId(accountId);
        Subscription subscription;

        if (existingSub.isPresent()) {
            subscription = existingSub.get();
            subscription.setPlanId(plan.getId());
            subscription.setStripeSubscriptionId(command.stripeSubscriptionId());
            subscription.setStatus(SubscriptionStatus.valueOf(command.status().toUpperCase()));
            subscription.setCurrentPeriodStart(command.currentPeriodStart());
            subscription.setCurrentPeriodEnd(command.currentPeriodEnd());
            subscription.setCancelAtPeriodEnd(command.cancelAtPeriodEnd());
        } else {
            subscription = Subscription.builder()
                    .id(UUID.randomUUID().toString())
                    .accountId(accountId)
                    .planId(plan.getId())
                    .stripeSubscriptionId(command.stripeSubscriptionId())
                    .status(SubscriptionStatus.valueOf(command.status().toUpperCase()))
                    .currentPeriodStart(command.currentPeriodStart())
                    .currentPeriodEnd(command.currentPeriodEnd())
                    .cancelAtPeriodEnd(command.cancelAtPeriodEnd())
                    .build();
        }

        Subscription saved = subscriptionRepository.save(subscription);
        log.info("Subscription persisted locally: id={}, accountId={}, planId={}", saved.getId(), command.accountId(), plan.getId());
        return Optional.of(saved);
    }

    @Override
    public String createCheckoutSession(String accountIdStr, String planId) {
        AccountId accountId = new AccountId(accountIdStr);
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription Account not found for accountId: " + accountIdStr));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with ID: " + planId));

        if (account.getStripeCustomerId() == null || account.getStripeCustomerId().isBlank()) {
            log.info("Creating Stripe Customer for account: {}", accountIdStr);
            String stripeCustomerId = paymentService.createCustomer(account.getEmail(), accountIdStr, accountIdStr);
            account.setStripeCustomerId(stripeCustomerId);
            accountRepository.save(account);
        }

        log.info("Generating Stripe Checkout Session for account={}, plan={}", accountIdStr, planId);
        return paymentService.createCheckoutSession(
                account.getStripeCustomerId(),
                plan.getStripePriceId(),
                accountIdStr,
                planId,
                successUrl,
                cancelUrl
        );
    }

    @Override
    public void handleStripeWebhook(String payload, String sigHeader) {
        Event event;
        try {
            if (webhookSecret == null || webhookSecret.isBlank() || webhookSecret.equals("whsec_WebhookSecretPlaceholderSecretKey")) {
                log.info("Bypassing Stripe signature verification (dev environment fallback)");
                event = Event.GSON.fromJson(payload, Event.class);
            } else {
                event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            }
        } catch (Exception e) {
            log.error("Signature verification failed: {}", e.getMessage());
            throw new RuntimeException("Webhook Signature Verification Failed", e);
        }

        log.info("Processing Stripe Webhook Event: {}", event.getType());

        switch (event.getType()) {
            case "checkout.session.completed":
                handleCheckoutSessionCompleted(event);
                break;
            case "customer.subscription.updated":
                handleSubscriptionUpdated(event);
                break;
            case "customer.subscription.deleted":
                handleSubscriptionDeleted(event);
                break;
            default:
                log.info("Unhandled event type: {}", event.getType());
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
        if (session == null) {
            log.warn("Checkout Session object is null");
            return;
        }

        String resolvedAccountIdStr = session.getClientReferenceId();
        if (resolvedAccountIdStr == null) {
            resolvedAccountIdStr = session.getMetadata() != null ? session.getMetadata().get("accountId") : null;
        }
        final String accountIdStr = resolvedAccountIdStr;
        String planId = session.getMetadata() != null ? session.getMetadata().get("planId") : null;
        String stripeSubscriptionId = session.getSubscription();
        String stripeCustomerId = session.getCustomer();

        if (accountIdStr == null || planId == null || stripeSubscriptionId == null) {
            log.warn("Missing checkout metadata: accountId={}, planId={}, stripeSubscriptionId={}", accountIdStr, planId, stripeSubscriptionId);
            return;
        }

        log.info("Checkout completed. AccountId: {}, PlanId: {}, StripeSubscriptionId: {}", accountIdStr, planId, stripeSubscriptionId);

        try {
            // Fetch Stripe subscription details via SDK or simulate default values
            com.stripe.model.Subscription stripeSub = com.stripe.model.Subscription.retrieve(stripeSubscriptionId);

            CreateSubscriptionCommand cmd = new CreateSubscriptionCommand(
                    accountIdStr,
                    planId,
                    stripeSubscriptionId,
                    stripeCustomerId,
                    stripeSub.getStatus(),
                    Instant.ofEpochSecond(stripeSub.getCurrentPeriodStart()),
                    Instant.ofEpochSecond(stripeSub.getCurrentPeriodEnd()),
                    stripeSub.getCancelAtPeriodEnd()
            );

            handle(cmd);

            // Update Account info
            AccountId accountId = new AccountId(accountIdStr);
            Account account = accountRepository.findByAccountId(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountIdStr));
            account.setCurrentPlanId(planId);
            account.setStripeCustomerId(stripeCustomerId);
            account.setStatus(mapStripeStatusToAccountStatus(stripeSub.getStatus()));
            accountRepository.save(account);

        } catch (Exception e) {
            log.error("Error processing checkout session completion", e);
        }
    }

    private void handleSubscriptionUpdated(Event event) {
        com.stripe.model.Subscription stripeSub = (com.stripe.model.Subscription) event.getDataObjectDeserializer().getObject().orElse(null);
        if (stripeSub == null) {
            log.warn("Subscription object is null");
            return;
        }

        log.info("Subscription updated. Stripe ID: {}, Status: {}", stripeSub.getId(), stripeSub.getStatus());

        Optional<Subscription> localSubOpt = subscriptionRepository.findByStripeSubscriptionId(stripeSub.getId());
        if (localSubOpt.isPresent()) {
            Subscription localSub = localSubOpt.get();
            localSub.setStatus(SubscriptionStatus.valueOf(stripeSub.getStatus().toUpperCase()));
            localSub.setCurrentPeriodStart(Instant.ofEpochSecond(stripeSub.getCurrentPeriodStart()));
            localSub.setCurrentPeriodEnd(Instant.ofEpochSecond(stripeSub.getCurrentPeriodEnd()));
            localSub.setCancelAtPeriodEnd(stripeSub.getCancelAtPeriodEnd());
            subscriptionRepository.save(localSub);

            // Update Account status
            Optional<Account> accountOpt = accountRepository.findByAccountId(localSub.getAccountId());
            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                account.setStatus(mapStripeStatusToAccountStatus(stripeSub.getStatus()));
                accountRepository.save(account);
            }
        }
    }

    private void handleSubscriptionDeleted(Event event) {
        com.stripe.model.Subscription stripeSub = (com.stripe.model.Subscription) event.getDataObjectDeserializer().getObject().orElse(null);
        if (stripeSub == null) {
            log.warn("Subscription object is null");
            return;
        }

        log.info("Subscription deleted / canceled. Stripe ID: {}", stripeSub.getId());

        Optional<Subscription> localSubOpt = subscriptionRepository.findByStripeSubscriptionId(stripeSub.getId());
        if (localSubOpt.isPresent()) {
            Subscription localSub = localSubOpt.get();
            localSub.setStatus(SubscriptionStatus.CANCELED);
            subscriptionRepository.save(localSub);

            Optional<Account> accountOpt = accountRepository.findByAccountId(localSub.getAccountId());
            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                account.setStatus(AccountStatus.CANCELED);
                account.setCurrentPlanId(null);
                accountRepository.save(account);
            }
        }
    }

    private AccountStatus mapStripeStatusToAccountStatus(String stripeStatus) {
        switch (stripeStatus) {
            case "active":
                return AccountStatus.ACTIVE;
            case "trialing":
                return AccountStatus.TRIALING;
            case "canceled":
            case "incomplete_expired":
                return AccountStatus.CANCELED;
            default:
                return AccountStatus.INACTIVE;
        }
    }
}
