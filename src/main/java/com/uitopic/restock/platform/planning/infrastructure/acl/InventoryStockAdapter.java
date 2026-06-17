package com.uitopic.restock.platform.planning.infrastructure.acl;

import com.uitopic.restock.platform.planning.domain.services.InventoryStockPort;
import com.uitopic.restock.platform.resources.interfaces.acl.ResourcesContextFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Anti-Corruption Layer (ACL) adapter implementing {@link InventoryStockPort}.
 *
 * <p>Bridges the {@code planning} bounded context with the {@code resources} bounded
 * context via an in-process call to {@link ResourcesContextFacade} — the public
 * inbound ACL boundary exposed by {@code resources}.</p>
 */
@Slf4j
@Component
public class InventoryStockAdapter implements InventoryStockPort {

    private final ResourcesContextFacade resourcesContextFacade;

    public InventoryStockAdapter(ResourcesContextFacade resourcesContextFacade) {
        this.resourcesContextFacade = resourcesContextFacade;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Delegates to {@link ResourcesContextFacade#getTotalStockByCustomSupplyIdAndBranchId(String, String)}.</p>
     */
    @Override
    public double getTotalStock(String customSupplyId, String branchId) {
        log.debug("Fetching total stock for customSupplyId={}, branchId={}", customSupplyId, branchId);
        return resourcesContextFacade.getTotalStockByCustomSupplyIdAndBranchId(customSupplyId, branchId);
    }
}
