package com.uitopic.restock.platform.planning.interfaces.acl;

import java.util.List;

public interface PlanningContextFacade {
    List<IngredientData> getProductIngredients(String productId);
    boolean productExists(String productId);

    record IngredientData(
            String customSupplyId,
            Double quantityPerUnit
    ) {}
}
