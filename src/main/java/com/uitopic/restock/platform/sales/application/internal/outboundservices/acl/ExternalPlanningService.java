package com.uitopic.restock.platform.sales.application.internal.outboundservices.acl;


import com.uitopic.restock.platform.planning.interfaces.acl.PlanningContextFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Acts as an Outbound Anti-Corruption Layer (ACL) adapter for the Planning Context.
 */
@Slf4j
@Service
public class ExternalPlanningService {

    private final PlanningContextFacade planningContextFacade;

    public ExternalPlanningService(PlanningContextFacade planningContextFacade) {
        this.planningContextFacade = planningContextFacade;
    }

    public record IngredientRequirement(String customSupplyId, Double quantityPerUnit) {}

    public List<IngredientRequirement> getProductIngredients(String productId) {
        return planningContextFacade.getProductIngredients(productId)
                .stream()
                .map(ing -> new IngredientRequirement(
                        ing.customSupplyId(),
                        ing.quantityPerUnit()
                ))
                .toList();
    }
}