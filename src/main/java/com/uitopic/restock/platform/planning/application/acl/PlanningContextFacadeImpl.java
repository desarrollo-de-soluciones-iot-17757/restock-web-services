package com.uitopic.restock.platform.planning.application.acl;

import com.uitopic.restock.platform.planning.domain.model.queries.GetProductByIdQuery;
import com.uitopic.restock.platform.planning.domain.services.ProductQueryService;
import com.uitopic.restock.platform.planning.interfaces.acl.PlanningContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanningContextFacadeImpl implements PlanningContextFacade {

    private final ProductQueryService productQueryService;

    public PlanningContextFacadeImpl(ProductQueryService productQueryService) {
        this.productQueryService = productQueryService;
    }

    @Override
    public List<IngredientData> getProductIngredients(String productId) {
        return productQueryService.handle(new GetProductByIdQuery(productId))
                .map(product -> product.getIngredients().stream()
                        .map(ing -> new IngredientData(
                                ing.getCustomSupplyId(),
                                ing.getQuantity()
                        ))
                        .toList()
                )
                .orElse(List.of());
    }

    @Override
    public boolean productExists(String productId) {
        return productQueryService.handle(new GetProductByIdQuery(productId)).isPresent();
    }
}