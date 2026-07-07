package com.uitopic.restock.platform.tracking.interfaces.rest.controllers;

import com.uitopic.restock.platform.tracking.domain.services.TelemetryReadingCommandService;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.TelemetryReadingResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TelemetriesControllerTest {

    @Mock
    private TelemetryReadingCommandService telemetryReadingCommandService;

    @InjectMocks
    private TelemetriesController telemetriesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveTelemetrySuccess() {
        // Arrange
        var resource = new TelemetryReadingResource(
                10.0,
                23.5,
                55.0,
                "batch-123",
                "device-123",
                "2024-06-15T14:30:00Z"
        );

        doNothing().when(telemetryReadingCommandService).handle(any());

        // Act
        telemetriesController.receiveTelemetry(resource);

        // Assert
        verify(telemetryReadingCommandService, times(1)).handle(any());
    }
}
