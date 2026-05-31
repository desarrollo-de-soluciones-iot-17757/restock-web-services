package com.uitopic.restock.platform.resources.domain.model.entities;

import com.uitopic.restock.platform.resources.domain.model.valueobjects.Stock;
import com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Entity representing a stock deduction event recorded against an
 * {@link Inventory} within the resources bounded context.
 *
 * <p>Captures the quantity removed from an inventory on a specific date, providing
 * an audit trail of consumption or waste events. Each deduction is linked to
 * the {@link Inventory} from which stock was removed.
 *
 * <p>Extends
 * {@link com.uitopic.restock.platform.shared.domain.model.entities.AuditableModel}
 * to inherit {@code createdAt} and {@code updatedAt} audit timestamps.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inventory_deductions")
public class InventoryDeduction extends AuditableModel {

    /** The identifier of the branch from which stock was deducted. */
    private String branchId;

    /** The inventory from which stock was deducted. */
    private Inventory inventory;

    /** The quantity of stock that was removed in this deduction event. */
    private Stock quantityDeducted;

    /** The date on which this deduction was recorded. */
    private LocalDate deductionDate;
}
