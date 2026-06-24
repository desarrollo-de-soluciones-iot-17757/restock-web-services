package com.uitopic.restock.platform.communications.application.internal.commandservices;

import com.uitopic.restock.platform.communications.domain.model.aggregates.Notification;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationSeverity;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.NotificationStatus;
import com.uitopic.restock.platform.communications.domain.model.valueobjects.SourceType;
import com.uitopic.restock.platform.communications.domain.repositories.NotificationRepository;
import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.entities.NotificationPersistenceEntity;
import com.uitopic.restock.platform.communications.infrastructure.persistence.mongodb.repositories.NotificationPersistenceRepository;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.StockThresholdEvaluationResult;
import com.uitopic.restock.platform.resources.domain.model.aggregates.Batch;
import com.uitopic.restock.platform.resources.domain.model.aggregates.CustomSupply;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.resources.domain.model.valueobjects.StockRange;
import com.uitopic.restock.platform.resources.domain.repositories.BatchRepository;
import com.uitopic.restock.platform.resources.domain.repositories.CustomSupplyRepository;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class StockThresholdEvaluationServiceImplTest {

    @Mock
    private CustomSupplyRepository customSupplyRepository;

    @Mock
    private BatchRepository batchRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationPersistenceRepository notificationPersistenceRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    private StockThresholdEvaluationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new StockThresholdEvaluationServiceImpl(
                customSupplyRepository,
                batchRepository,
                notificationRepository,
                notificationPersistenceRepository,
                mongoTemplate,
                true);
    }

    @Test
    void testIsActiveAndToggle() {
        assertTrue(service.isActive());
        service.setActive(false);
        assertFalse(service.isActive());
    }

    @Test
    void testEvaluateStockThresholds_ServiceNotActive() {
        service.setActive(false);

        List<StockThresholdEvaluationResult> results = service.evaluateStockThresholds();

        assertTrue(results.isEmpty());
        verifyNoInteractions(customSupplyRepository);
    }

    @Test
    void testEvaluateStockThresholds_NoThresholds() {
        CustomSupply supply = mock(CustomSupply.class);
        when(supply.getStockRange()).thenReturn(null);

        when(customSupplyRepository.findAll()).thenReturn(List.of(supply));

        List<StockThresholdEvaluationResult> results = service.evaluateStockThresholds();

        assertTrue(results.isEmpty());
        verifyNoInteractions(batchRepository);
    }

    @Test
    void testEvaluateStockThresholds_ExcessStock_NewAlert() {
        // Arrange
        CustomSupply supply = mock(CustomSupply.class);
        when(supply.getId()).thenReturn("supply-123");
        when(supply.getName()).thenReturn("Coffee");
        when(supply.getAccountId()).thenReturn(new AccountId("account-999"));
        when(supply.getStockRange()).thenReturn(new StockRange(0.0, 10.0));

        UnitMeasurement unit = mock(UnitMeasurement.class);
        when(unit.unitName()).thenReturn("Kg");

        Batch batch = mock(Batch.class);
        when(batch.getCurrentStock()).thenReturn(new Stock(15.0, unit));

        when(customSupplyRepository.findAll()).thenReturn(List.of(supply));
        when(batchRepository.findByCustomSupplyId("supply-123")).thenReturn(List.of(batch));

        when(mongoTemplate.find(any(Query.class), eq(NotificationPersistenceEntity.class)))
                .thenReturn(Collections.emptyList());

        Notification savedNotification = new Notification(
                "account-999", "supply-123", SourceType.INVENTORY,
                "Product Coffee has exceeded its maximum stock limit of 10.0. Current stock: 15.0",
                "Excess Stock Alert", NotificationSeverity.WARNING.name());
        savedNotification.setId("notification-123");

        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        // Act
        List<StockThresholdEvaluationResult> results = service.evaluateStockThresholds();

        // Assert
        assertEquals(1, results.size());
        StockThresholdEvaluationResult result = results.get(0);
        assertEquals("supply-123", result.customSupplyId());
        assertEquals("Coffee", result.customSupplyName());
        assertEquals(15.0, result.currentStock());
        assertEquals(10.0, result.maxStock());
        assertEquals("EXCESS_STOCK", result.status());
        assertEquals("notification-123", result.alertId());

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        verify(notificationRepository, times(1)).save(captor.capture());
        Notification captured = captor.getValue();
        assertEquals("account-999", captured.getRecipientId());
        assertEquals("supply-123", captured.getSourceId());
        assertEquals(SourceType.INVENTORY, captured.getSourceType());
        assertEquals(NotificationSeverity.WARNING, captured.getSeverity());
    }

    @Test
    void testEvaluateStockThresholds_ExcessStock_AlertAlreadyActive() {
        // Arrange
        CustomSupply supply = mock(CustomSupply.class);
        when(supply.getId()).thenReturn("supply-123");
        when(supply.getName()).thenReturn("Coffee");
        when(supply.getAccountId()).thenReturn(new AccountId("account-999"));
        when(supply.getStockRange()).thenReturn(new StockRange(0.0, 10.0));

        UnitMeasurement unit = mock(UnitMeasurement.class);
        when(unit.unitName()).thenReturn("Kg");

        Batch batch = mock(Batch.class);
        when(batch.getCurrentStock()).thenReturn(new Stock(15.0, unit));

        when(customSupplyRepository.findAll()).thenReturn(List.of(supply));
        when(batchRepository.findByCustomSupplyId("supply-123")).thenReturn(List.of(batch));

        NotificationPersistenceEntity activeAlert = new NotificationPersistenceEntity();
        activeAlert.setId("active-notif-123");
        when(mongoTemplate.find(any(Query.class), eq(NotificationPersistenceEntity.class)))
                .thenReturn(List.of(activeAlert));

        // Act
        List<StockThresholdEvaluationResult> results = service.evaluateStockThresholds();

        // Assert
        assertEquals(1, results.size());
        StockThresholdEvaluationResult result = results.get(0);
        assertEquals("active-notif-123", result.alertId());
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void testEvaluateStockThresholds_NormalStock_DeactivateAlert() {
        // Arrange
        CustomSupply supply = mock(CustomSupply.class);
        when(supply.getId()).thenReturn("supply-123");
        when(supply.getName()).thenReturn("Coffee");
        when(supply.getAccountId()).thenReturn(new AccountId("account-999"));
        when(supply.getStockRange()).thenReturn(new StockRange(0.0, 10.0));

        UnitMeasurement unit = mock(UnitMeasurement.class);
        when(unit.unitName()).thenReturn("Kg");

        Batch batch = mock(Batch.class);
        when(batch.getCurrentStock()).thenReturn(new Stock(5.0, unit));

        when(customSupplyRepository.findAll()).thenReturn(List.of(supply));
        when(batchRepository.findByCustomSupplyId("supply-123")).thenReturn(List.of(batch));

        NotificationPersistenceEntity activeAlert = new NotificationPersistenceEntity();
        activeAlert.setId("active-notif-123");
        activeAlert.setStatus(NotificationStatus.UNREAD);
        when(mongoTemplate.find(any(Query.class), eq(NotificationPersistenceEntity.class)))
                .thenReturn(List.of(activeAlert));

        // Act
        List<StockThresholdEvaluationResult> results = service.evaluateStockThresholds();

        // Assert
        assertEquals(1, results.size());
        StockThresholdEvaluationResult result = results.get(0);
        assertEquals("NORMAL", result.status());
        assertNull(result.alertId());

        verify(notificationPersistenceRepository, times(1)).save(activeAlert);
        assertEquals(NotificationStatus.READ, activeAlert.getStatus());
    }
}
