package com.uitopic.restock.platform.communications.interfaces.rest.controllers;

import com.uitopic.restock.platform.communications.domain.services.StockThresholdEvaluationService;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.StockThresholdEvaluationResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST controller for stock threshold alert evaluation.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/alerts/stock-thresholds", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Stock Threshold Alerts", description = "Endpoints related to stock threshold alert evaluation.")
public class StockThresholdAlertsController {

    private final StockThresholdEvaluationService stockThresholdEvaluationService;

    public StockThresholdAlertsController(StockThresholdEvaluationService stockThresholdEvaluationService) {
        this.stockThresholdEvaluationService = stockThresholdEvaluationService;
    }

    /**
     * Triggers the stock threshold evaluation process.
     * Evaluates all product stock levels against maximum limits, generating alerts where necessary.
     *
     * @return list of stock threshold evaluation results.
     */
    @PostMapping("/evaluate")
    @Operation(
            summary = "Evaluate stock thresholds",
            description = "Triggers stock threshold evaluations for all custom supplies and generates/deactivates excess stock alerts."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluation executed successfully"),
            @ApiResponse(responseCode = "400", description = "Evaluation service is not active")
    })
    public ResponseEntity<List<StockThresholdEvaluationResult>> evaluate() {
        log.info("Received request to evaluate stock thresholds.");
        if (!stockThresholdEvaluationService.isActive()) {
            log.warn("Evaluation request failed: Stock threshold evaluation service is not active.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Evaluation service is not active");
        }
        List<StockThresholdEvaluationResult> results = stockThresholdEvaluationService.evaluateStockThresholds();
        log.info("Stock threshold evaluation completed successfully. Evaluated {} custom supplies.", results.size());
        return ResponseEntity.ok(results);
    }
}
