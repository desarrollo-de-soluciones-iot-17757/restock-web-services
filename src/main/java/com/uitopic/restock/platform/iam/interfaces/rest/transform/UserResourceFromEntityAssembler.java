package com.uitopic.restock.platform.iam.interfaces.rest.transform;

import com.uitopic.restock.platform.iam.domain.model.aggregates.User;
import com.uitopic.restock.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class UserResourceFromEntityAssembler {

    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(
                user.getId(),
                user.getEmail().email(),
                user.getRole().getType().name(),
                token
        );
    }
}
