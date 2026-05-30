package com.uitopic.restock.platform.resources.domain.model.commands;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;

import java.time.LocalDate;
import java.util.Optional;

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
