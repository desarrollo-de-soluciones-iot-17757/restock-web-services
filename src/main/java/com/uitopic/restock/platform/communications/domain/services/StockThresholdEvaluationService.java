package com.uitopic.restock.platform.communications.domain.services;

import com.uitopic.restock.platform.communications.interfaces.rest.resources.StockThresholdEvaluationResult;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import java.util.List;

/**
 * Service port interface for stock threshold evaluations.
 */
public interface StockThresholdEvaluationService {

    /**
     * Evaluates stock levels for all custom supplies against their maximum limits,
     * generating excess stock alerts for those in overstock, and deactivating existing
     * alerts for those returning to normal levels.
     *
     * @return a list of evaluation results for all custom supplies checked.
     */
    List<StockThresholdEvaluationResult> evaluateStockThresholds();

    /**
     * Evaluates stock levels for custom supplies belonging to a specific account.
     *
     * @param accountId the account to evaluate stock thresholds for.
     * @return a list of evaluation results for the account's custom supplies.
     */
    List<StockThresholdEvaluationResult> evaluateStockThresholds(AccountId accountId);

    /**
     * Checks if the evaluation service is active.
     *
     * @return true if the service is active, false otherwise.
     */
    boolean isActive();

    /**
     * Sets the active status of the evaluation service.
     * Useful for enabling/disabling the service dynamically or during testing.
     *
     * @param active true to enable the service, false to disable.
     */
    void setActive(boolean active);
}
