package com.uitopic.restock.platform.communications.application.internal.queryservices;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.domain.model.queries.GetNotificationByIdQuery;
import com.uitopic.restock.platform.communications.domain.model.queries.GetNotificationsByRecipientUserIdQuery;
import com.uitopic.restock.platform.communications.domain.services.NotificationQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {


    @Override
    public Optional<Notification> handle(GetNotificationByIdQuery query) {
        return Optional.empty();
    }

    @Override
    public List<Notification> handle(GetNotificationsByRecipientUserIdQuery query) {
        return List.of();
    }
}
