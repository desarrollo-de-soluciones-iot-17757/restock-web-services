package com.uitopic.restock.platform.tracking.application.internal.commandservices;

import com.uitopic.restock.platform.tracking.domain.model.aggregates.PhysicalAnomaly;
import com.uitopic.restock.platform.tracking.domain.model.commands.CreatePhysicalAnomalyCommand;
import com.uitopic.restock.platform.tracking.domain.repositories.PhysicalAnomalyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PhysicalAnomalyCommandServiceImplTest {

    @Mock
    private PhysicalAnomalyRepository physicalAnomalyRepository;

    @InjectMocks
    private PhysicalAnomalyCommandServiceImpl physicalAnomalyCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleCreatePhysicalAnomalySuccess() {
        // Arrange
        var command = new CreatePhysicalAnomalyCommand("device-123", 512.0, Instant.parse("2026-07-04T12:00:00Z"));

        when(physicalAnomalyRepository.save(any(PhysicalAnomaly.class))).thenAnswer(invocation -> {
            PhysicalAnomaly anomaly = invocation.getArgument(0);
            anomaly.setId("anomaly-id-123");
            return anomaly;
        });

        // Act
        var result = physicalAnomalyCommandService.handle(command);

        // Assert
        assertNotNull(result);
        assertEquals("anomaly-id-123", result.getId());
        assertEquals("device-123", result.getDeviceId().getDeviceId());
        assertEquals(512.0, result.getRegisteredValue());
        assertEquals(Instant.parse("2026-07-04T12:00:00Z"), result.getTimestamp());
        verify(physicalAnomalyRepository, times(1)).save(any(PhysicalAnomaly.class));
    }

    @Test
    void testHandleDefaultsTimestampWhenNotProvided() {
        // Arrange
        var command = new CreatePhysicalAnomalyCommand("device-456", 300.0, null);

        when(physicalAnomalyRepository.save(any(PhysicalAnomaly.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = physicalAnomalyCommandService.handle(command);

        // Assert
        assertNotNull(result.getTimestamp());
        verify(physicalAnomalyRepository, times(1)).save(any(PhysicalAnomaly.class));
    }

    @Test
    void testCreatePhysicalAnomalyCommandRejectsBlankDeviceId() {
        assertThrows(IllegalArgumentException.class, () -> new CreatePhysicalAnomalyCommand(" ", 100.0, null));
        verifyNoInteractions(physicalAnomalyRepository);
    }

    @Test
    void testCreatePhysicalAnomalyCommandRejectsNullRegisteredValue() {
        assertThrows(IllegalArgumentException.class, () -> new CreatePhysicalAnomalyCommand("device-789", null, null));
        verifyNoInteractions(physicalAnomalyRepository);
    }
}
