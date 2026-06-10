package com.uitopic.restock.platform.communications.application.internal.commandservices;

import com.uitopic.restock.platform.communications.domain.model.aggregates.PushSubscription;
import com.uitopic.restock.platform.communications.domain.model.commands.RegisterPushSubscriptionCommand;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.ClientPlatform;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationProvider;
import com.uitopic.restock.platform.communications.domain.repositories.PushSubscriptionRepository;
import com.uitopic.restock.platform.communications.domain.services.PushSubscriptionCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the PushSubscriptionCommandService interface for handling push subscription commands.
 * This service is responsible for processing commands related to push subscriptions, such as registering new subscriptions.
 */
@Slf4j
@Service
public class PushSubscriptionCommandServiceImpl implements PushSubscriptionCommandService {

    /** Repository for managing push subscriptions. */
    private final PushSubscriptionRepository pushSubscriptionRepository;

    /** Constructor for dependency injection of the PushSubscriptionRepository. */
    public PushSubscriptionCommandServiceImpl(PushSubscriptionRepository pushSubscriptionRepository) {
        this.pushSubscriptionRepository = pushSubscriptionRepository;
    }


    @Override
    public Optional<PushSubscription> handle(RegisterPushSubscriptionCommand command) {
        log.info("Registering push subscription. userId={}, platform={}, provider={}",
                command.userId(), command.clientPlatform(), command.provider());

        var pushSubscription = pushSubscriptionRepository.findByProviderToken(command.providerToken())
                .map(existing -> {
                    existing.setUserId(command.userId());
                    existing.setPlatform(ClientPlatform.valueOf(command.clientPlatform()));
                    existing.setProvider(NotificationProvider.valueOf(command.provider()));
                    existing.setActive(true);
                    return existing;
                })
                .orElseGet(() -> new PushSubscription(
                        command.userId(),
                        command.providerToken(),
                        command.clientPlatform(),
                        command.provider()
                ));

        return Optional.of(pushSubscriptionRepository.save(pushSubscription));
    }
}
