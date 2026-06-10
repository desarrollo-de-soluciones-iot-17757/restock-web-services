package com.uitopic.restock.platform.communications.interfaces.rest.controllers;

import com.uitopic.restock.platform.communications.domain.services.PushSubscriptionCommandService;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.PushSubscriptionResource;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.RegisterPushSubscriptionResource;
import com.uitopic.restock.platform.communications.interfaces.rest.transform.PushSubscriptionResourceFromEntityAssembler;
import com.uitopic.restock.platform.communications.interfaces.rest.transform.RegisterPushSubscriptionCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * REST controller for managing push notification subscriptions.
 * This controller provides endpoints for registering device tokens for push notifications.
 */
@RestController
@RequestMapping(value = "/api/v1/push-subscriptions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Push Subscriptions", description = "Device token registration for push notifications.")
public class PushSubscriptionsController {

    /** Service for handling push subscription commands */
    private final PushSubscriptionCommandService pushSubscriptionCommandService;

    /** Constructor for PushSubscriptionsController, injecting the PushSubscriptionCommandService */
    public PushSubscriptionsController(PushSubscriptionCommandService pushSubscriptionCommandService) {
        this.pushSubscriptionCommandService = pushSubscriptionCommandService;
    }

    /**
     * Endpoint for registering a push notification subscription.
     * Accepts a JSON payload containing the device token and other relevant information.
     * Returns a response with the created push subscription resource or an error if registration fails.
     *
     * @param resource The request body containing the push subscription details.
     * @return A ResponseEntity containing the created PushSubscriptionResource or an error status.
     */
    @Operation(summary = "Register a push notification subscription")
    @PostMapping()
    public ResponseEntity<PushSubscriptionResource> register(
            @Valid @RequestBody RegisterPushSubscriptionResource resource
    ) {
        var command = RegisterPushSubscriptionCommandFromResourceAssembler.toCommandFromResource(resource);
        var pushSubscription = pushSubscriptionCommandService.handle(command)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Push subscription could not be registered"
                ));

        return ResponseEntity
                .created(URI.create("/api/v1/push-subscriptions/" + pushSubscription.getId()))
                .body(PushSubscriptionResourceFromEntityAssembler.toResourceFromEntity(pushSubscription));
    }
}
