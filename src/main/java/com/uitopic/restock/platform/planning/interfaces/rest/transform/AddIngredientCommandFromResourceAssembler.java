package com.uitopic.restock.platform.planning.interfaces.rest.transform;

import com.uitopic.restock.platform.planning.domain.model.commands.AddIngredientCommand;
import com.uitopic.restock.platform.planning.interfaces.rest.resources.AddIngredientResource;

/**
 * Assembler that converts an {@link AddIngredientResource} REST request DTO
 * into an {@link AddIngredientCommand} domain command within the {@code planning} bounded context.
 */
public class AddIngredientCommandFromResourceAssembler {

    /**
     * Transforms an {@link AddIngredientResource} and the path-variable {@code productId}
     * into an {@link AddIngredientCommand}.
     *
     * @param productId the ID of the target {@code Product} aggregate (from the URL path)
     * @param resource  the incoming REST request resource; must not be {@code null}
     * @return an {@link AddIngredientCommand} ready to be handled by the command service
     */
    public static AddIngredientCommand toCommandFromResource(String productId,
                                                             AddIngredientResource resource) {
        return new AddIngredientCommand(
                productId,
                resource.customSupplyId(),
                resource.quantity()
        );
    }
}
