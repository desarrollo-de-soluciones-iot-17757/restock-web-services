package com.uitopic.restock.platform.communications.interfaces.rest.controllers;

import com.uitopic.restock.platform.communications.domain.model.queries.GetNotificationByIdQuery;
import com.uitopic.restock.platform.communications.domain.model.queries.GetNotificationsByRecipientUserIdQuery;
import com.uitopic.restock.platform.communications.domain.services.NotificationQueryService;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.NotificationResource;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.NotificationWrapperResource;
import com.uitopic.restock.platform.communications.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import com.uitopic.restock.platform.communications.interfaces.rest.transform.NotificationWrapperFromEntitiesAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for notification retrieval by identifier.
 */
@RestController
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "Endpoints related to persisted in-app notifications.")
public class NotificationsController {

    /**
     * Query service used to retrieve notifications.
     */
    private final NotificationQueryService notificationQueryService;

    /**
     * Creates the controller with its query dependency.
     *
     * @param notificationQueryService notification query service
     */
    public NotificationsController(NotificationQueryService notificationQueryService) {
        this.notificationQueryService = notificationQueryService;
    }

    /**
     * Retrieves all persisted notifications for a recipient user.
     *
     * @param recipientUserId recipient user identifier
     * @return notification wrapper with persisted notifications for the recipient user
     */
    @GetMapping
    @Operation(
            summary = "Get notifications by recipient user",
            description = "Retrieves persisted in-app notifications by recipient user identifier."
    )
    @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully")
    public ResponseEntity<NotificationWrapperResource> getNotificationsByRecipientUserId(
            @RequestParam String recipientUserId
    ) {
        var notifications = notificationQueryService.handle(
                new GetNotificationsByRecipientUserIdQuery(recipientUserId)
        );

        return ResponseEntity.ok(
                NotificationWrapperFromEntitiesAssembler.toResourceFromEntities(notifications)
        );
    }

    /**
     * Retrieves one persisted notification by id.
     *
     * @param notificationId notification identifier
     * @return notification if it exists
     */
    @GetMapping("/{notificationId}")
    @Operation(
            summary = "Get notification by ID",
            description = "Retrieves one persisted in-app notification by its identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    public ResponseEntity<NotificationResource> getNotificationById(@PathVariable String notificationId) {
        var notification = notificationQueryService.handle(new GetNotificationByIdQuery(notificationId));

        return notification
                .map(entity -> ResponseEntity.ok(NotificationResourceFromEntityAssembler.toResourceFromEntity(entity)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
