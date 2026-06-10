package com.uitopic.restock.platform.communications.infrastructure.adapters;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.domain.repositories.NotificationRepository;
import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.assemblers.NotificationPersistenceAssembler;
import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.repositories.NotificationPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of the NotificationRepository interface for managing notifications.
 * This class provides methods to save notifications and retrieve them based on recipient user ID or notification ID.
 * Currently, the methods return null or empty lists as placeholders and should be implemented with actual data persistence logic.
 */
@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    /** MongoDB repository for notifications. */
    private final NotificationPersistenceRepository notificationMongoRepository;

    /**
     * Constructs a NotificationRepositoryImpl with the specified NotificationPersistenceRepository.
     *
     * @param notificationMongoRepository the MongoDB repository for notifications
     */
    public NotificationRepositoryImpl(NotificationPersistenceRepository notificationMongoRepository) {
        this.notificationMongoRepository = notificationMongoRepository;
    }

    /**
     * Saves a notification to the repository.
     *
     * @param notification the notification to save
     * @return the saved notification with any generated fields (e.g., ID)
     */
    @Override
    public Notification save(Notification notification) {
        var saved = notificationMongoRepository.save(NotificationPersistenceAssembler.toPersistenceFromDomain(notification));
        return NotificationPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Finds notifications by the recipient user ID.
     *
     * @param recipientUserId the user ID of the notification recipient
     * @return a list of notifications for the specified recipient user ID
     */
    @Override
    public List<Notification> findByRecipientUserId(String recipientUserId) {
        return notificationMongoRepository.findByRecipientId(recipientUserId)
                .stream()
                .map(NotificationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    /**
     * Finds a notification by its unique identifier.
     *
     * @param notificationId the unique identifier of the notification
     * @return the notification with the specified ID, or null if not found
     */
    @Override
    public Notification findById(String notificationId) {
        return notificationMongoRepository.findById(notificationId)
                .map(NotificationPersistenceAssembler::toDomainFromPersistence)
                .orElse(null);
    }
}
