package com.uitopic.restock.platform.shared.infrastructure.filestorage.cloudinary;

import com.uitopic.restock.platform.shared.application.internal.outboundservices.filestorage.ImageService;

/**
 * Interface for image storage service that extends the ImageService.
 * This interface can be implemented by any cloud storage provider, such as Cloudinary.
 */
public interface CloudinaryStorageService extends ImageService {
}
