package com.uitopic.restock.platform.resources.domain.model.commands;

/**
 * Command to create a new custom supply within the resources bounded context.
 */
public record CreateCustomSupplyCommand(
		String accountId,
		String supplyId,
		String name,
		String description,
		double unitPrice,
		String unitPriceCurrencyCode,
		double supplyContent,
		String unitMeasurement,
		byte[] image,
		String imageFileName
) {
	/**
	 * Check if image data is provided in the command.
	 *
	 * @return true if image array and fileName are not null, false otherwise
	 */
	public boolean hasImage() {
		return image != null && imageFileName != null;
	}
}
