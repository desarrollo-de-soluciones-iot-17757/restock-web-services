package com.uitopic.restock.platform.communications.application.internal.queryservices;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.domain.model.queries.GetNotificationByIdQuery;
import com.uitopic.restock.platform.communications.domain.model.queries.GetNotificationsByRecipientUserIdQuery;
import com.uitopic.restock.platform.communications.domain.repositories.NotificationRepository;
import com.uitopic.restock.platform.communications.domain.services.NotificationQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the NotificationQueryService interface for handling notification queries.
 * This service is responsible for processing queries related to notifications, such as retrieving notifications by ID or by recipient user ID.
 */
@Slf4j
@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {

    /** Repository for managing notifications */
    private final NotificationRepository notificationRepository;

    /** Constructor for dependency injection of the NotificationRepository */
    public NotificationQueryServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Query handler for retrieving a notification by its ID.
     */
    @Override
    public Optional<Notification> handle(GetNotificationByIdQuery query) {
        return Optional.ofNullable(notificationRepository.findById(query.notificationId()));
    }

    /**
     * Query handler for retrieving notifications by recipient user ID.
     */
    @Override
    public List<Notification> handle(GetNotificationsByRecipientUserIdQuery query) {
        return notificationRepository.findByRecipientUserId(query.recipientUserId());
    }
}
