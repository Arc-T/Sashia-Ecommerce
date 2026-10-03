package com.sashia.ecommerce.media.dto;

import com.sashia.ecommerce.media.MediaResourceType;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Multipart form. {@code resourceType} and {@code resourceId} are optional but must be sent together:
 * leave them out while the resource does not exist yet (product being created).
 */
public record MediaUploadRequest(
        MultipartFile[] files,
        @Nullable MediaResourceType resourceType,
        @Nullable Long resourceId,
        @Nullable @Size(max = 400, message = "media.description.size") String description
) {
}
