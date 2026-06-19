package com.uitopic.restock.platform.profiles.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Schema(name = "CreateBusinessResource", description = "Request resource for creating a business")
public record CreateBusinessResource(
        @NotBlank String userId,
        String ruc,
        @NotBlank String companyName,
        String mainLocation,
        MultipartFile image
) {}
