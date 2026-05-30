package com.uitopic.restock.platform.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(
        String id,
        String email,
        String role,
        String token
) {
}
