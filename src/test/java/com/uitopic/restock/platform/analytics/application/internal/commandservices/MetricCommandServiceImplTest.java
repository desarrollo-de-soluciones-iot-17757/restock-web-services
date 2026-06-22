package com.uitopic.restock.platform.analytics.application.internal.commandservices;

import com.uitopic.restock.platform.analytics.domain.model.aggregates.Metric;
import com.uitopic.restock.platform.analytics.domain.model.commands.RegisterMetricCommand;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.DateRange;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.uitopic.restock.platform.analytics.domain.model.valueobjects.MetricType;
import com.uitopic.restock.platform.analytics.domain.repositories.MetricRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MetricCommandServiceImplTest {

    @Mock
    private MetricRepository metricRepository;

    @InjectMocks
    private MetricCommandServiceImpl metricCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleRegisterMetricSuccess() {
        // Arrange
        var command = new RegisterMetricCommand(
                MetricCategory.INVENTORY,
                MetricType.LOW_STOCK_SUPPLIES,
                new DateRange(LocalDate.of(2026, 6, 20), LocalDate.of(2026, 6, 21)),
                "account-123"
        );

        var savedMetric = Metric.builder()
                .category(command.category())
                .type(command.type())
                .dateRange(command.dateRange())
                .accountId(command.accountId())
                .build();
        savedMetric.setId("metric-id-123");

        when(metricRepository.save(any(Metric.class))).thenReturn(savedMetric);

        // Act
        var result = metricCommandService.handle(command);

        // Assert
        assertNotNull(result);
        assertEquals("metric-id-123", result.getId());
        assertEquals(MetricCategory.INVENTORY, result.getCategory());
        assertEquals(MetricType.LOW_STOCK_SUPPLIES, result.getType());
        assertEquals("account-123", result.getAccountId().accountId());
        verify(metricRepository, times(1)).save(any(Metric.class));
    }
}
