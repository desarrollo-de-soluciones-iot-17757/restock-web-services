package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BatchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.shared.infrastructure.eventpublisher.spring.SpringDomainEventPublisher;
import com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl.ExternalDevicesService;
import com.uitopic.restock.platform.tracking.application.internal.outboundservices.acl.ExternalResourcesService;
import com.uitopic.restock.platform.tracking.domain.exceptions.TelemetryValuesException;
import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;
import com.uitopic.restock.platform.tracking.domain.model.commands.ReceiveTelemetryReadingCommand;
import com.uitopic.restock.platform.tracking.domain.model.entities.TelemetryReading;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.HumidityRecord;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.StockRecord;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.TemperatureRecord;
import com.uitopic.restock.platform.tracking.domain.repositories.StockComparisonTaskRepository;
import com.uitopic.restock.platform.tracking.domain.repositories.TelemetryReadingRepository;
import com.uitopic.restock.platform.tracking.domain.services.ConciliationTaskCommandService;
import com.uitopic.restock.platform.resources.interfaces.acl.ResourceStockSnapshot;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.uitopic.restock.platform.tracking.domain.model.commands.ClosePendingConciliationTasksCommand;

class TelemetryReadingCommandServiceImplTest {

    @Mock
    private TelemetryReadingRepository telemetryReadingRepository;

    @Mock
    private StockComparisonTaskRepository stockComparisonTaskRepository;

    @Mock
    private ExternalDevicesService externalDevicesService;

    @Mock
    private ExternalResourcesService externalResourcesService;

    @Mock
    private SpringDomainEventPublisher domainEventPublisher;

    @Mock
    private ConciliationTaskCommandService conciliationTaskCommandService;

    @InjectMocks
    private TelemetryReadingCommandServiceImpl telemetryReadingCommandService;

    private DeviceId deviceId;
    private BatchId batchId;
    private ReceiveTelemetryReadingCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deviceId = new DeviceId("device-123");
        batchId = new BatchId("batch-123");
        command = new ReceiveTelemetryReadingCommand(
                new StockRecord(10.0),
                new TemperatureRecord(23.5),
                new HumidityRecord(55.0),
                batchId,
                deviceId
        );
    }

    @Test
    void testHandleUnknownDeviceThrowsException() {
        // Arrange
        when(externalDevicesService.deviceExists(deviceId)).thenReturn(false);

        // Act & Assert
        assertThrows(TelemetryValuesException.class, () -> {
            telemetryReadingCommandService.handle(command);
        });
        verify(telemetryReadingRepository, never()).save(any(TelemetryReading.class));
    }

    @Test
    void testHandleSuccessWithoutAnomaly() {
        // Arrange
        when(externalDevicesService.deviceExists(deviceId)).thenReturn(true);

        var telemetryReading = new TelemetryReading(command);
        when(telemetryReadingRepository.save(any(TelemetryReading.class))).thenReturn(telemetryReading);

        var snapshot = new ResourceStockSnapshot(10.0, "supply-123", "Custom Supply", "branch-123", "account-123");
        when(externalResourcesService.getStockSnapshotByBatchId(batchId)).thenReturn(snapshot);
        when(externalDevicesService.getJustifiedWithdrawnStock(deviceId)).thenReturn(Pair.of(0.0, new AccountId("account-123")));

        var task = mock(StockComparisonTask.class);
        when(task.isAnomalyDetected()).thenReturn(false);
        when(task.getTotalPhysicalStock()).thenReturn(10.0);
        when(task.getDeviceId()).thenReturn(deviceId);
        when(stockComparisonTaskRepository.save(any(StockComparisonTask.class))).thenReturn(task);

        // Act
        telemetryReadingCommandService.handle(command);

        // Assert
        verify(telemetryReadingRepository, times(1)).save(any(TelemetryReading.class));
        verify(stockComparisonTaskRepository, times(1)).save(any(StockComparisonTask.class));
        verify(conciliationTaskCommandService, times(1)).handle(any(ClosePendingConciliationTasksCommand.class));
        verify(domainEventPublisher, never()).publish(any());
    }

    @Test
    void testHandleSuccessWithAnomaly() {
        // Arrange
        when(externalDevicesService.deviceExists(deviceId)).thenReturn(true);

        var telemetryReading = new TelemetryReading(command);
        when(telemetryReadingRepository.save(any(TelemetryReading.class))).thenReturn(telemetryReading);

        var snapshot = new ResourceStockSnapshot(15.0, "supply-123", "Custom Supply", "branch-123", "account-123");
        when(externalResourcesService.getStockSnapshotByBatchId(batchId)).thenReturn(snapshot);
        when(externalDevicesService.getJustifiedWithdrawnStock(deviceId)).thenReturn(Pair.of(0.0, new AccountId("account-123")));

        var task = mock(StockComparisonTask.class);
        when(task.isAnomalyDetected()).thenReturn(true);
        when(task.getDifference()).thenReturn(-5.0);
        when(task.getDeviceId()).thenReturn(deviceId);
        when(stockComparisonTaskRepository.save(any(StockComparisonTask.class))).thenReturn(task);

        // Act
        telemetryReadingCommandService.handle(command);

        // Assert
        verify(telemetryReadingRepository, times(1)).save(any(TelemetryReading.class));
        verify(stockComparisonTaskRepository, times(1)).save(any(StockComparisonTask.class));
        verify(domainEventPublisher, times(1)).publish(any());
        verify(conciliationTaskCommandService, never()).handle(any(ClosePendingConciliationTasksCommand.class));
    }
}
