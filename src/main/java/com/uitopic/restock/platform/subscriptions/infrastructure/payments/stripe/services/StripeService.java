package com.uitopic.restock.platform.subscriptions.infrastructure.payments.stripe.services;

import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.uitopic.restock.platform.subscriptions.application.internal.outboundservices.payments.PaymentService;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class StripeService implements PaymentService {

    @Override
    public String createCustomer(String email, String name, String accountId) {
        try {
            CustomerCreateParams params = CustomerCreateParams.builder()
                    .setEmail(email)
                    .setName(name)
                    .putMetadata("accountId", accountId)
                    .build();
            Customer customer = Customer.create(params);
            return customer.getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Stripe customer", e);
        }
    }

    @Override
    public String createCheckoutSession(String stripeCustomerId, String stripePriceId, String accountId, String planId, String successUrl, String cancelUrl) {
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setCustomer(stripeCustomerId)
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setPrice(stripePriceId)
                            .setQuantity(1L)
                            .build())
                    .putMetadata("accountId", accountId)
                    .putMetadata("planId", planId)
                    .setClientReferenceId(accountId)
                    .build();
            Session session = Session.create(params);
            return session.getUrl();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Stripe checkout session", e);
        }
    }

    @Override
    public Map<String, String> getSessionMetadata(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            return session.getMetadata();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve Stripe session metadata", e);
        }
    }
}
