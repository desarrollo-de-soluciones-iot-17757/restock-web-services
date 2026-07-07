package com.uitopic.restock.platform.tracking.interfaces.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uitopic.restock.platform.analytics.application.internal.queryservices.AnalyticsReportingQueryServiceImpl;
import com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceThresholdsByAccountIdQuery;
import com.uitopic.restock.platform.devices.domain.services.DeviceCommandService;
import com.uitopic.restock.platform.devices.domain.services.DeviceQueryService;
import com.uitopic.restock.platform.devices.domain.services.DeviceThresholdCommandService;
import com.uitopic.restock.platform.devices.domain.services.DeviceThresholdQueryService;
import com.uitopic.restock.platform.tracking.domain.services.TelemetryReadingCommandService;
import com.uitopic.restock.platform.tracking.interfaces.rest.resources.TelemetryReadingResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class TelemetriesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TelemetryReadingCommandService telemetryReadingCommandService;

    @MockitoBean
    private DeviceThresholdCommandService deviceThresholdCommandService;

    @MockitoBean
    private DeviceThresholdQueryService deviceThresholdQueryService;

    @MockitoBean
    private DeviceCommandService deviceCommandService;

    @MockitoBean
    private DeviceQueryService deviceQueryService;

    @MockitoBean
    private AnalyticsReportingQueryServiceImpl analyticsReportingQueryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testReceiveTelemetryEndpoint_IT_BE_01() throws Exception {
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

        // Act & Assert
        mockMvc.perform(post("/api/v1/telemetries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk());

        verify(telemetryReadingCommandService, times(1)).handle(any());
    }

    @Test
    void testGetDeviceThresholdsEndpoint_IT_BE_02() throws Exception {
        // Arrange
        when(deviceThresholdQueryService.handle(any(GetDeviceThresholdsByAccountIdQuery.class)))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/device-thresholds")
                        .param("accountId", "account-123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetDeviceByIdEndpoint_IT_BE_03() throws Exception {
        // Arrange
        when(deviceQueryService.handle(any(com.uitopic.restock.platform.devices.domain.model.queries.GetDeviceByIdQuery.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/devices/device-123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCriticalProductsEndpoint_IT_BE_04() throws Exception {
        // Arrange
        when(analyticsReportingQueryService.getCriticalProducts("account-123"))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/accounts/account-123/critical-products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetStockDiscrepanciesEndpoint_IT_BE_05() throws Exception {
        // Arrange
        when(analyticsReportingQueryService.getStockDiscrepancies("prod-1"))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/custom-supplies/prod-1/stock-discrepancies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isConciliated").value(true));
    }
}
