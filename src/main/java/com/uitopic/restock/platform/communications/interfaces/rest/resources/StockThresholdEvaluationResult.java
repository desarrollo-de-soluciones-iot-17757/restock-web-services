package com.uitopic.restock.platform.communications.interfaces.rest.resources;

/**
 * Resource representing the result of evaluating stock thresholds for a custom
 * supply.
 * Contains information about the evaluated product, stock level, limit, status,
 * and generated alert ID if any.
 */
public record StockThresholdEvaluationResult(
                String customSupplyId,
                String customSupplyName,
                Double currentStock,
                Double maxStock,
                String status,
                String alertId) {
}
