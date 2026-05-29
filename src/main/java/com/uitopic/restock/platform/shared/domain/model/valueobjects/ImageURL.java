package com.uitopic.restock.platform.shared.domain.model.valueobjects;

/**
 * Value object representing an image URL.
 *
 * @param url the URL string of the image
 */
public record ImageURL(
        String url
) {

    // Regular expression to validate URL format
    private static final String URL_REGEX = "^(https?|ftp)://[^\\s/$.?#].\\S*$";

    // Validation logic in the constructor
    public ImageURL {
        if (url == null || url.isBlank() || !url.matches(URL_REGEX)) {
            throw new IllegalArgumentException("Image URL cannot be null or blank");
        }
    }

    /**
     * Returns the URL string.
     *
     * @return the URL string
     */
    public String getUrl() {
        return url;
    }
}
