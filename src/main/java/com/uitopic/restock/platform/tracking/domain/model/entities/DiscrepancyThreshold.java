package com.uitopic.restock.platform.tracking.domain.model.entities;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.AccountId;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.BranchId;
import com.uitopic.restock.platform.tracking.domain.model.valueobjects.DeviceId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entity representing a discrepancy threshold for inventory tracking.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@EqualsAndHashCode
public class DiscrepancyThreshold {

    /**
     * The ID is required to uniquely identify the discrepancy threshold and to prevent unauthorized access or modifications.
     */
    @NotBlank
    private String id;

    /**
     * The threshold difference is required to define the maximum allowed discrepancy between the expected and actual stock levels before triggering an alert or notification. It helps to ensure that the system can effectively monitor inventory levels and take appropriate actions when discrepancies occur.
     */
    @NotNull
    private Stock thresholdDifference;

    /**
     * The device ID is required to ensure that the discrepancy threshold is associated with the correct device and to prevent unauthorized access or modifications.
     */
    @NotNull
    private DeviceId deviceId;

    /**
     * The branch id is required to ensure that the discrepancy threshold is associated with the correct branch and to prevent unauthorized access or modifications.
     */
    @NotNull
    private BranchId branchId;
}
