package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.model.aggregates.StockComparisonTask;
import com.uitopic.restock.platform.tracking.domain.model.commands.ClosePendingConciliationTasksCommand;
import com.uitopic.restock.platform.tracking.domain.model.commands.CreateConciliationTaskCommand;
import com.uitopic.restock.platform.tracking.domain.model.commands.RegisterDiscrepancyCommand;
import com.uitopic.restock.platform.tracking.domain.model.entities.Discrepancy;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ComparisonResult;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DiscrepancyAlertLevel;
import com.uitopic.restock.platform.tracking.domain.repositories.DiscrepancyRepository;
import com.uitopic.restock.platform.tracking.domain.services.ConciliationTaskCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DiscrepancyCommandServiceImplTest {

    @Mock
    private DiscrepancyRepository discrepancyRepository;

    @Mock
    private ConciliationTaskCommandService conciliationTaskCommandService;

    @InjectMocks
    private DiscrepancyCommandServiceImpl discrepancyCommandService;

    private StockComparisonTask stockComparisonTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockComparisonTask = mock(StockComparisonTask.class);
        when(stockComparisonTask.getId()).thenReturn("stock-comparison-task-123");
        when(stockComparisonTask.getResult()).thenReturn(ComparisonResult.MISMATCH);
        when(stockComparisonTask.getDeviceId()).thenReturn(new DeviceId("device-123"));
    }

    @Test
    void testHandleRegisterDiscrepancyWithRiskLevelOkNoConciliationCreated() {
        // Arrange
        var command = new RegisterDiscrepancyCommand(stockComparisonTask, DiscrepancyAlertLevel.OK);
        var discrepancy = new Discrepancy(stockComparisonTask, DiscrepancyAlertLevel.OK);
        
        when(discrepancyRepository.save(any(Discrepancy.class))).thenReturn(discrepancy);

        // Act
        discrepancyCommandService.handle(command);

        // Assert
        verify(discrepancyRepository, times(1)).save(any(Discrepancy.class));
        verify(conciliationTaskCommandService, never()).handle(any(CreateConciliationTaskCommand.class));
        verify(conciliationTaskCommandService, never()).handle(any(ClosePendingConciliationTasksCommand.class));
    }

    @Test
    void testHandleRegisterDiscrepancyWithRiskLevelCriticalConciliationCreated() {
        // Arrange
        var command = new RegisterDiscrepancyCommand(stockComparisonTask, DiscrepancyAlertLevel.CRITICAL);
        var discrepancy = new Discrepancy(stockComparisonTask, DiscrepancyAlertLevel.CRITICAL);

        when(discrepancyRepository.save(any(Discrepancy.class))).thenReturn(discrepancy);

        // Act
        discrepancyCommandService.handle(command);

        // Assert
        verify(discrepancyRepository, times(1)).save(any(Discrepancy.class));
        verify(conciliationTaskCommandService, times(1)).handle(any(CreateConciliationTaskCommand.class));
    }
}
