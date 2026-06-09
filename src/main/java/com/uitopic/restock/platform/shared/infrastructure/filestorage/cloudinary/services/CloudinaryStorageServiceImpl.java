package com.uitopic.restock.platform.shared.infrastructure.filestorage.cloudinary.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.uitopic.restock.platform.shared.infrastructure.filestorage.cloudinary.CloudinaryStorageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the CloudinaryStorageService interface.
 * This class provides methods to upload and delete images using Cloudinary.
 * Currently, the methods are stubbed and return empty results or do nothing.
 * In a real implementation, these methods would interact with the Cloudinary API
 * to perform the necessary operations.
 */
@Service
public class CloudinaryStorageServiceImpl implements CloudinaryStorageService {

    /**
     * Cloudinary instance used to interact with the Cloudinary API.
     * This instance is typically configured with API credentials and other settings.
     */
    private final Cloudinary cloudinary;

    /**
     * Constructor for CloudinaryStorageServiceImpl.
     *
     * @param cloudinary The Cloudinary instance to be used for API interactions.
     */
    public CloudinaryStorageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Uploads a file to Cloudinary and returns a map containing the URL and public ID of the uploaded file.
     *
     * @param fileData The byte array representing the file data to be uploaded.
     * @param fileName The original name of the file being uploaded.
     * @return A map containing the URL and public ID of the uploaded file.
     */
    @Override
    public Map<String, String> upload(byte[] fileData, String fileName) {
        try {
            String uniqueFileName = generateUniqueFileName(fileName);

            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "public_id", uniqueFileName,
                    "resource_type", "auto",
                    "folder", "profiles"
            );

            Map result = cloudinary.uploader().upload(fileData, uploadParams);

            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");

            Map<String, String> response = new HashMap<>();
            response.put("url", url);
            response.put("publicId", publicId);

            return response;

        } catch (IOException e) {
            throw new RuntimeException("Error uploading file to Cloudinary: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a file from Cloudinary using its public ID.
     *
     * @param publicId The public ID of the file to be deleted.
     */
    @Override
    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file from Cloudinary: " + e.getMessage(), e);
        }
    }

    /**
     * Generates a unique file name by appending a timestamp to the original file name (without extension).
     *
     * @param originalFileName The original name of the file.
     * @return A unique file name based on the original name and current timestamp.
     */
    private String generateUniqueFileName(String originalFileName) {
        String nameWithoutExtension = removeExtension(originalFileName);
        long timestamp = System.currentTimeMillis();
        return nameWithoutExtension + "_" + timestamp;
    }

    /**
     * Removes the file extension from a given file name.
     *
     * @param fileName The original file name from which to remove the extension.
     * @return The file name without its extension. If the input is null or empty, returns "file".
     */
    private String removeExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "file";
        }

        int lastDotIndex = fileName.lastIndexOf('.');

        if (lastDotIndex == -1) {
            return fileName;
        }

        return fileName.substring(0, lastDotIndex);
    }
}
