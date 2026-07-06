package com.uitopic.restock.platform.tracking.interfaces.rest.controllers;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.application.internal.commandservices.PhysicalAnomalyCommandService;
import com.uitopic.restock.platform.tracking.domain.model.aggregates.PhysicalAnomaly;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.CreatePhysicalAnomalyResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnomaliesControllerTest {

    @Mock
    private PhysicalAnomalyCommandService physicalAnomalyCommandService;

    @InjectMocks
    private AnomaliesController anomaliesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReportAnomalySuccess() {
        // Arrange
        var resource = new CreatePhysicalAnomalyResource("device-123", 512.0, Instant.parse("2026-07-04T12:00:00Z"));

        var savedAnomaly = new PhysicalAnomaly(new DeviceId("device-123"), 512.0, Instant.parse("2026-07-04T12:00:00Z"));
        savedAnomaly.setId("anomaly-id-123");

        when(physicalAnomalyCommandService.handle(any())).thenReturn(savedAnomaly);

        // Act
        var response = anomaliesController.reportAnomaly(resource);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("anomaly-id-123", response.getBody().id());
        assertEquals("device-123", response.getBody().deviceId());
        assertEquals(512.0, response.getBody().registeredValue());
        verify(physicalAnomalyCommandService, times(1)).handle(any());
    }
}
