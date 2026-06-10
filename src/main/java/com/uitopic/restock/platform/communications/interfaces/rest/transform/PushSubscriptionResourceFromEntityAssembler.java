package com.uitopic.restock.platform.communications.interfaces.rest.transform;

import com.uitopic.restock.platform.communications.domain.model.aggregates.PushSubscription;
import com.uitopic.restock.platform.communications.interfaces.rest.resources.PushSubscriptionResource;

/**
 * Assembler class responsible for converting a PushSubscription entity into a PushSubscriptionResource.
 * This resource is used to represent the push subscription data in the REST API responses.
 */
public class PushSubscriptionResourceFromEntityAssembler {

    /**
     * Converts a PushSubscription entity into a PushSubscriptionResource.
     *
     * @param entity The PushSubscription entity to convert.
     * @return A PushSubscriptionResource containing the data from the entity, formatted for REST API responses.
     */
    public static PushSubscriptionResource toResourceFromEntity(PushSubscription entity) {
        return new PushSubscriptionResource(
                entity.getId(),
                entity.getUserId(),
                entity.getPlatform().name(),
                entity.getProvider().name(),
                entity.isActive()
        );
    }
}
