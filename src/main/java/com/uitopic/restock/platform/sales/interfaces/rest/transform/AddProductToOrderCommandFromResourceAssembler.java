package com.uitopic.restock.platform.sales.interfaces.rest.transform;

import com.uitopic.restock.platform.sales.domain.model.commands.AddProductToOrderCommand;
import com.uitopic.restock.platform.sales.domain.model.valueobjects.ProductType;
import com.uitopic.restock.platform.sales.interfaces.rest.resources.AddProductToOrderResource;
import java.math.BigDecimal;

public class AddProductToOrderCommandFromResourceAssembler {
    public static AddProductToOrderCommand toCommandFromResource(String orderId, AddProductToOrderResource resource) {
        return new AddProductToOrderCommand(
                orderId,
                resource.productId(),
                ProductType.valueOf(resource.productType().toUpperCase()),
                resource.nameSnapshot(),
                BigDecimal.valueOf(resource.unitPrice()),
                resource.currency(),
                resource.quantity()
        );
    }
}