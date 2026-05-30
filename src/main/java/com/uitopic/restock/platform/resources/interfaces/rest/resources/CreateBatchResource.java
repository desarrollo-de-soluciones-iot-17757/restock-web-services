package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Optional;

@Schema(description = "Request resource for creating a batch")
public record CreateBatchResource(
        @NotBlank @Schema(description = "Batch code")
        String code,
        @Min(0) @Schema(description = "Current quantity")
        Integer initialStock,
        @NotBlank @Schema(description = "Unit purchase cost amount")
        String unitPurchaseCostAmount,
        @NotBlank @Schema(description = "Unit purchase cost currency")
        String unitPurchaseCostCurrency,
        @NotBlank @Schema(description = "Custom supply ID")
        String customSupplyId,
        @NotBlank @Schema(description = "Branch ID")
        String receivingBranchId,
        @NotBlank @Schema(description = "Account ID")
        String accountId,
        @Schema(description = "Manufacturing date (ISO format)")
        Optional<String> manufacturingDate,
        @Schema(description = "Expiration date (ISO format)")
        Optional<String> expirationDate,
        @Schema(description = "Entry date (ISO format)")
        Optional<String> entryDate
) {}
