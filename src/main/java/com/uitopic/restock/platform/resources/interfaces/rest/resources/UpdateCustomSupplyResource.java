package com.uitopic.restock.platform.resources.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request resource for updating a custom supply within the resources bounded
 * context.
 *
 * <p>
 * Sent as a JSON part inside a multipart/form-data request. The image file
 * is sent as a separate part.
 *
 * @param supplyId              the supply template ID
 * @param name                  the name of the custom supply
 * @param description           (optional) the description of the custom supply
 * @param unitPrice             the unit price amount
 * @param unitPriceCurrencyCode the currency code
 * @param supplyContent         the supply content (quantity per unit)
 * @param unitMeasurement       the unit of measurement
 */
@Schema(description = "Request resource for updating a custom supply")
public record UpdateCustomSupplyResource(
                @Schema(description = "Supply template ID") String supplyId,
                @Schema(description = "Name") String name,
                @Schema(description = "Description") String description,
                @Schema(description = "Unit price amount", example = "10.10") double unitPrice,
                @Schema(description = "Currency code", example = "PEN") String unitPriceCurrencyCode,
                @Schema(description = "Supply content (quantity per unit)") double supplyContent,
                @Schema(description = "Unit of measurement") String unitMeasurement,
                @Schema(description = "Image file") MultipartFile image) {
        public boolean hasImage() {
                return this.image != null
                                && !this.image.isEmpty()
                                && this.image.getOriginalFilename() != null
                                && !this.image.getOriginalFilename().isBlank();
        }
}
