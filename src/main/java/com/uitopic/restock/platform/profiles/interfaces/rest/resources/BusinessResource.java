package com.uitopic.restock.platform.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "BusinessResource", description = "Response resource representing a business")
public record BusinessResource(
        String id,
        String userId,
        String ruc,
        String pictureUrl,
        String picturePublicId,
        String companyName,
        String mainLocation
) {}
