package com.uitopic.restock.platform.planning.interfaces.rest.transform;

import com.uitopic.restock.platform.planning.domain.model.commands.CreateProductCommand;
import com.uitopic.restock.platform.planning.interfaces.rest.resources.CreateProductResource;

/**
 * Assembler that converts a {@link CreateProductResource} REST request DTO
 * into a {@link CreateProductCommand} domain command within the {@code planning} bounded context.
 */
public class CreateProductCommandFromResourceAssembler {

    /**
     * Transforms a {@link CreateProductResource} into a {@link CreateProductCommand}.
     *
     * @param resource the incoming REST request resource; must not be {@code null}
     * @return a {@link CreateProductCommand} ready to be handled by the command service
     */
    public static CreateProductCommand toCommandFromResource(CreateProductResource resource) {
        return new CreateProductCommand(
                resource.accountId(),
                resource.name(),
                resource.description(),
                resource.sku(),
                resource.type(),
                resource.imageUrl(),
                resource.sellingPrice()
        );
    }
}
