package com.uitopic.restock.platform.communications.interfaces.rest.controllers;

import com.uitopic.restock.platform.communications.domain.services.StockThresholdEvaluationService;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.StockThresholdEvaluationResult;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockThresholdAlertsControllerTest {

    @Mock
    private StockThresholdEvaluationService stockThresholdEvaluationService;

    @InjectMocks
    private StockThresholdAlertsController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEvaluateSuccess() {
        // Arrange
        var accountId = "account-1";
        when(stockThresholdEvaluationService.isActive()).thenReturn(true);
        var expectedResults = List.of(
                new StockThresholdEvaluationResult("supply-1", "Product A", 15.0, 10.0, "EXCESS_STOCK", "alert-1")
        );
        when(stockThresholdEvaluationService.evaluateStockThresholds(new AccountId(accountId))).thenReturn(expectedResults);

        // Act
        ResponseEntity<List<StockThresholdEvaluationResult>> response = controller.evaluate(accountId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResults, response.getBody());
        verify(stockThresholdEvaluationService, times(1)).evaluateStockThresholds(new AccountId(accountId));
    }

    @Test
    void testEvaluateServiceInactiveThrowsException() {
        // Arrange
        var accountId = "account-1";
        when(stockThresholdEvaluationService.isActive()).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> controller.evaluate(accountId));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Evaluation service is not active", exception.getReason());
        verify(stockThresholdEvaluationService, never()).evaluateStockThresholds(any());
    }
}
