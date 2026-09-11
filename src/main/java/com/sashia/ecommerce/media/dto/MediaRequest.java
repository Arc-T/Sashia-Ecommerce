package com.sashia.ecommerce.media.dto;

import com.sashia.ecommerce.media.Media;

import java.time.LocalDateTime;

public record MediaRequest(
        Long id,
        Long productId,
        String url,
        String type,
        String extension,
        LocalDateTime createdAt
) {

    public MediaRequest(Long productId,
                        String url,
                        String type,
                        String extension,
                        LocalDateTime createdAt) {
        this(null, productId, url, type, extension, createdAt);
    }

    public static MediaRequest toDTO(Media media) {
        return new MediaRequest(
                media.getId(),
                media.getResourceId(),
                media.getSlug(),
                media.getMimeType(),
                media.getExtension(),
                media.getCreatedAt()
        );
    }

}
