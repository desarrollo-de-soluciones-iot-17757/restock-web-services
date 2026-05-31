package com.uitopic.restock.platform.resources.domain.model.commands;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Command to create a new batch of supplies in the inventory system.
 *
 * @param code the unique code for the batch
 * @param initialStock the initial stock quantity for the batch
 * @param unitPurchaseCost the cost per unit for purchasing the supplies in the batch
 * @param customSupplyId the custom identifier for the supply associated with the batch
 * @param receivingBranchId the identifier for the branch receiving the batch
 * @param accountId the identifier for the account responsible for the batch
 * @param manufacturingDate the optional manufacturing date of the supplies in the batch
 * @param expirationDate the optional expiration date of the supplies in the batch
 * @param entryDate the optional date when the batch was entered into the system
 */
public record CreateBatchCommand(
        String code,
        Stock initialStock,
        Money unitPurchaseCost,
        String customSupplyId,
        String receivingBranchId,
        AccountId accountId,
        Optional<LocalDate> manufacturingDate,
        Optional<LocalDate> expirationDate,
        Optional<LocalDate> entryDate
) {}