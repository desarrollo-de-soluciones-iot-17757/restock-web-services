package com.uitopic.restock.platform.communications.application.internal.commandservices;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationSeverity;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationStatus;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.SourceType;
import com.uitopic.restock.platform.communications.domain.repositories.NotificationRepository;
import com.uitopic.restock.platform.communications.domain.services.StockThresholdEvaluationService;
import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.entities.NotificationPersistenceEntity;
import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.repositories.NotificationPersistenceRepository;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.StockThresholdEvaluationResult;
import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for evaluating stock thresholds and managing excess stock alerts.
 */
@Slf4j
@Service
public class StockThresholdEvaluationServiceImpl implements StockThresholdEvaluationService {

    private final CustomSupplyRepository customSupplyRepository;
    private final BatchRepository batchRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationPersistenceRepository notificationPersistenceRepository;
    private final MongoTemplate mongoTemplate;

    private boolean active;

    public StockThresholdEvaluationServiceImpl(
            CustomSupplyRepository customSupplyRepository,
            BatchRepository batchRepository,
            NotificationRepository notificationRepository,
            NotificationPersistenceRepository notificationPersistenceRepository,
            MongoTemplate mongoTemplate,
            @Value("${app.stock-threshold-evaluation.enabled:true}") boolean active
    ) {
        this.customSupplyRepository = customSupplyRepository;
        this.batchRepository = batchRepository;
        this.notificationRepository = notificationRepository;
        this.notificationPersistenceRepository = notificationPersistenceRepository;
        this.mongoTemplate = mongoTemplate;
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public List<StockThresholdEvaluationResult> evaluateStockThresholds() {
        if (!active) {
            log.info("Stock threshold evaluation is disabled because the service is inactive.");
            return new ArrayList<>();
        }

        List<StockThresholdEvaluationResult> results = new ArrayList<>();
        List<CustomSupply> customSupplies = customSupplyRepository.findAll();
        log.info("Starting stock threshold evaluation. Found {} custom supplies to inspect.", customSupplies.size());

        for (CustomSupply customSupply : customSupplies) {
            if (customSupply.getStockRange() == null || customSupply.getStockRange().maxStock() == null) {
                continue;
            }

            double maxStock = customSupply.getStockRange().maxStock();
            List<Batch> batches = batchRepository.findByCustomSupplyId(customSupply.getId());
            double totalStock = batches.stream()
                    .mapToDouble(batch -> batch.getCurrentStock() != null ? batch.getCurrentStock().stock() : 0.0)
                    .sum();

            String status;
            String alertId = null;

            if (totalStock > maxStock) {
                status = "EXCESS_STOCK";

                Query query = new Query();
                query.addCriteria(Criteria.where("sourceId").is(customSupply.getId())
                        .and("sourceType").is(SourceType.INVENTORY)
                        .and("status").is(NotificationStatus.UNREAD));
                List<NotificationPersistenceEntity> unreadAlerts = mongoTemplate.find(query, NotificationPersistenceEntity.class);

                if (unreadAlerts.isEmpty()) {
                    Notification notification = new Notification(
                            customSupply.getAccountId().getAccountId(),
                            customSupply.getId(),
                            SourceType.INVENTORY,
                            "Product " + customSupply.getName() + " has exceeded its maximum stock limit of " + maxStock + ". Current stock: " + totalStock,
                            "Excess Stock Alert",
                            NotificationSeverity.WARNING.name()
                    );
                    Notification saved = notificationRepository.save(notification);
                    alertId = saved.getId();
                    log.info("Created excess stock alert for product '{}' (id: {}). Stock: {} / {}",
                            customSupply.getName(), customSupply.getId(), totalStock, maxStock);
                } else {
                    alertId = unreadAlerts.get(0).getId();
                    log.info("Excess stock alert already active for product '{}'. Stock: {} / {}",
                            customSupply.getName(), totalStock, maxStock);
                }
            } else {
                status = "NORMAL";
                Query query = new Query();
                query.addCriteria(Criteria.where("sourceId").is(customSupply.getId())
                        .and("sourceType").is(SourceType.INVENTORY)
                        .and("status").is(NotificationStatus.UNREAD));
                List<NotificationPersistenceEntity> unreadAlerts = mongoTemplate.find(query, NotificationPersistenceEntity.class);

                if (!unreadAlerts.isEmpty()) {
                    for (NotificationPersistenceEntity entity : unreadAlerts) {
                        entity.setStatus(NotificationStatus.READ);
                        notificationPersistenceRepository.save(entity);
                        log.info("Deactivated excess stock alert for product '{}' because stock returned to normal: {} / {}",
                                customSupply.getName(), totalStock, maxStock);
                    }
                }
            }

            results.add(new StockThresholdEvaluationResult(
                    customSupply.getId(),
                    customSupply.getName(),
                    totalStock,
                    maxStock,
                    status,
                    alertId
            ));
        }

        return results;
    }
}
