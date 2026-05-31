package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

/**
 * Resource class representing the request body for creating a new branch. This class is used in the REST API to receive the necessary information for creating a branch, including account ID, branch name, address, location details, description, and an optional image file.
 */
@Schema(description = "Request resource for creating a branch")
public record CreateBranchResource(
        @NotBlank @Schema(description = "Branch name") String name,
        @NotBlank @Schema(description = "Branch address") String address,
        @NotBlank @Schema(description = "State or Region") String stateOrRegion,
        @NotBlank @Schema(description = "City") String city,
        @NotBlank @Schema(description = "Country") String country,
        @Schema(description = "Branch description") String description,
        @Schema(description = "Image") MultipartFile image
) {
    /** Method to check if the command includes a new photo for the branch. This method returns true if both the description and photoFileName fields are not null, indicating that a new photo is included in the update. */
    public boolean hasNewPhoto() {
        return this.image != null
                && !this.image.isEmpty()
                && this.image.getOriginalFilename() != null
                && !this.image.getOriginalFilename().isBlank();
    }
}
