package com.uitopic.restock.platform.sales.application.internal.outboundservices.acl;

import com.uitopic.restock.platform.resources.interfaces.acl.ResourcesContextFacade;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.BatchConsumption;
import com.uitopic.restock.platform.shared.domain.model.valueobjects.UnitMeasurement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for interacting with the resources context, allowing the sales application
 * to handle inventory modifications without being tightly coupled to the resources context's internal implementation.
 * Acts as an Outbound Anti-Corruption Layer (ACL) adapter.
 */
@Slf4j
@Service
public class ExternalResourcesService {

    private final ResourcesContextFacade resourcesContextFacade;

    /**
     * Constructor dependency injection for the resources context public facade.
     *
     * @param resourcesContextFacade the external resources context facade
     */
    public ExternalResourcesService(ResourcesContextFacade resourcesContextFacade) {
        this.resourcesContextFacade = resourcesContextFacade;
    }

    public void subtractStock(String branchId, String supplyId, Double quantity) {
        log.info("ACL: Realizando resta de stock real en Recursos...");

        resourcesContextFacade.subtractSupplyStock(branchId, supplyId, quantity.intValue());
    }

    /**
     * Resolves the optimal batch available for a specific supply in a branch,
     * and maps it to a domain-specific BatchConsumption Value Object.
     *
     * @param branchId       the branch identifier where stock is located
     * @param quantityNeeded the total decimal quantity required by the order item
     * @return a BatchConsumption value object ready to be attached to the Sales Order Item
     */

    public BatchConsumption resolveBatchConsumption(String branchId, String supplyId, Double quantityNeeded, String unitNameFromDB) {
        String batchId = resourcesContextFacade.resolveAvailableBatchId(branchId, supplyId, quantityNeeded);

        UnitMeasurement unit = new UnitMeasurement(unitNameFromDB);

        return new BatchConsumption(
                batchId,
                supplyId,
                quantityNeeded,
                unit
        );
    }
    /**
     * Deducts specific physical stock from a targeted batch in the Resources context.
     * Aligns directly with the calculated BatchConsumption value object.
     *
     * @param branchId          the branch identifier where the sale occurred
     * @param batchId           the specific physical batch identifier to subtract from
     * @param quantityToConsume the precise decimal quantity to deduct
     */
    public void subtractBatchStock(String branchId, String batchId, Double quantityToConsume) {
        log.info("ACL: Requesting batch stock reduction from Resources. branchId={}, batchId={}, quantity={}",
                branchId, batchId, quantityToConsume);

        try {
            double currentRemainingStock = resourcesContextFacade.subtractSupplyStock(
                    branchId,
                    batchId,
                    quantityToConsume.intValue()
            );

            log.debug("ACL: Stock successfully subtracted from batch. Current remaining stock: {}", currentRemainingStock);
        } catch (Exception e) {
            log.error("ACL: Error communicating with Resources Context during batch stock subtraction: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Restores stock back to a specific batch in case a Sales Order is cancelled or modified.
     *
     * @param branchId  the branch identifier
     * @param batchId   the specific batch identifier to restore
     * @param quantity  the decimal amount to return
     * @param unit      the measurement unit snapshot
     */
    public void rollBackBatchStock(String branchId, String batchId, Double quantity, String unit) {
        log.info("ACL: Requesting batch stock rollback to Resources. branchId={}, batchId={}, quantity={}, unit={}",
                branchId, batchId, quantity, unit);

        try {
            resourcesContextFacade.addSupplyStockBack(
                    branchId,
                    batchId,
                    quantity.intValue(),
                    unit
            );
            log.debug("ACL: Batch stock rollback successfully executed.");
        } catch (Exception e) {
            log.error("ACL: Error communicating with Resources Context during batch stock rollback: {}", e.getMessage());
        }
    }
}