package com.uitopic.restock.platform.tracking.domain.model.queries;

import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.DeviceId;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.ConciliationTaskStatus;

/**
 * Query to get conciliation tasks from a specific account using optional filters.
 *
 * @param accountId account identifier
 * @param status optional conciliation task status
 * @param customSupplyId optional custom supply identifier
 * @param branchId optional branch identifier
 * @param deviceId optional device identifier
 */
public record GetConciliationTasksByAccountIdQuery(
        AccountId accountId,
        ConciliationTaskStatus status,
        String customSupplyId,
        BranchId branchId,
        DeviceId deviceId
) {
    public GetConciliationTasksByAccountIdQuery {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
    }
}
