package com.uitopic.restock.platform.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Schema(name = "UpdateBusinessResource", description = "Request resource for partially updating a business")
public record UpdateBusinessResource(
        String ruc,
        String companyName,
        String mainLocation,
        MultipartFile image
) {}
