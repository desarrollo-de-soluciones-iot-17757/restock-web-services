package com.uitopic.restock.platform.resources.application.acl;

import com.uitopic.restock.platform.resources.domain.services.BatchCommandService;
import com.uitopic.restock.platform.resources.interfaces.acl.ResourcesContextFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ResourcesContextFacadeImpl implements ResourcesContextFacade {

    private final BatchCommandService batchCommandService;

    public ResourcesContextFacadeImpl(BatchCommandService batchCommandService) {
        this.batchCommandService = batchCommandService;
    }

    @Override
    public double subtractSupplyStock(String branchId, String supplyId, double quantity) {
        log.info("Attempting to subtract supply stock — branchId: {}, supplyId: {}, quantity: {}", branchId, supplyId, quantity);
        double remaining = batchCommandService.subtractStock(branchId, supplyId, quantity);
        log.info("Successfully subtracted supply stock — remaining stock: {}", remaining);
        return remaining;
    }

    @Override
    public void addSupplyStockBack(String branchId, String supplyId, double quantity, String unit) {
        log.info("Attempting to add supply stock back — branchId: {}, supplyId: {}, quantity: {}, unit: {}", branchId, supplyId, quantity, unit);
        batchCommandService.addStockBack(branchId, supplyId, quantity, unit);
        log.info("Successfully added supply stock back for supplyId: {}", supplyId);
    }

    @Override
    public void adjustStock(String branchId, String supplyId, double adjustedQuantity, String unit) {
        log.info("Attempting to adjust stock — branchId: {}, supplyId: {}, adjustedQuantity: {}, unit: {}", branchId, supplyId, adjustedQuantity, unit);
        batchCommandService.adjustStock(branchId, supplyId, adjustedQuantity, unit);
        log.info("Successfully adjusted stock for supplyId: {}", supplyId);
    }
}
