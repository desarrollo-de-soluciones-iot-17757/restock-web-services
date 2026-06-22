package com.uitopic.restock.platform.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProfileResource", description = "Response resource representing a user profile")
public record ProfileResource(
        String id,
        String accountId,
        String userId,
        String name,
        String lastName,
        String phoneNumber,
        String avatarUrl,
        String avatarPublicId,
        String gender,
        String birthDate
) {}
