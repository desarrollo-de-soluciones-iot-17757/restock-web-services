package com.uitopic.restock.platform.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ProfileResource",
        description = "Represents a user profile resource in the REST API."
)
public record ProfileResource() {
}
