package com.sashia.ecommerce.media.dto;

import com.sashia.ecommerce.media.MediaResourceType;
import com.sashia.ecommerce.media.MediaStatus;

import java.time.LocalDateTime;

public record MediaResponse(
        Long id,
        String url,
        String fileName,
        String mimeType,
        String extension,
//        MediaTypeEnum type,
        Long size,
        Integer width,
        Integer height,
        MediaStatus status,
        MediaResourceType resourceType,
        Long resourceId,
        Integer displayOrder,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
