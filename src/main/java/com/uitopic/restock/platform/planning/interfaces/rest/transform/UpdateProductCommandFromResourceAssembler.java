package com.uitopic.restock.platform.planning.interfaces.rest.transform;

import com.uitopic.restock.platform.planning.domain.model.commands.UpdateProductCommand;
import com.uitopic.restock.platform.planning.interfaces.rest.resources.UpdateProductResource;

/**
 * Assembler that converts an {@link UpdateProductResource} REST request DTO and the
 * path-variable {@code productId} into an {@link UpdateProductCommand} domain command
 * within the {@code planning} bounded context.
 */
public class UpdateProductCommandFromResourceAssembler {

    /**
     * Transforms an {@link UpdateProductResource} and the target product ID into an
     * {@link UpdateProductCommand}.
     *
     * @param productId the ID of the product to update (from the URL path)
     * @param resource  the incoming REST request resource; must not be {@code null}
     * @return an {@link UpdateProductCommand} ready to be handled by the command service
     */
    public static UpdateProductCommand toCommandFromResource(String productId,
                                                             UpdateProductResource resource) {
        return new UpdateProductCommand(
                productId,
                resource.name(),
                resource.description(),
                resource.sku(),
                resource.imageUrl(),
                resource.sellingPrice()
        );
    }
}
