package com.uitopic.restock.platform.resources.application.acl;

import com.uitopic.restock.platform.resources.domain.services.BatchCommandService;
import com.uitopic.restock.platform.resources.interfaces.acl.ResourcesContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ResourcesContextFacadeImpl implements ResourcesContextFacade {

    private final BatchCommandService batchCommandService;

    public ResourcesContextFacadeImpl(BatchCommandService batchCommandService) {
        this.batchCommandService = batchCommandService;
    }

    @Override
    public double subtractSupplyStock(String branchId, String supplyId, Integer quantity) {
        return 0;
    }

    @Override
    public void addSupplyStockBack(String branchId, String supplyId, Integer quantity, String unit) {

    }

    @Override
    public void adjustStock(String branchId, String supplyId, Integer adjustedQuantity, String unit) {

    }
}
