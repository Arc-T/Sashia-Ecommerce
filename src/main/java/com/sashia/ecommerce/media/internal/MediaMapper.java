package com.sashia.ecommerce.media.internal;

import com.sashia.ecommerce.media.Media;
import com.sashia.ecommerce.media.dto.MediaResponse;

import java.util.Map;

final class MediaMapper {

    /**
     * Public URL prefix of the file endpoint.
     */
    private static final String FILES_PATH = "/media/files/";

    private MediaMapper() {
    }

    public static MediaResponse toDTO(Media media) {
        return new MediaResponse(
                media.getId(),
                FILES_PATH + media.getSlug(),
                media.getOriginalName(),
                media.getMimeType(),
                media.getExtension(),
                media.getSize(),
                metadataInt(media.getMetadata(), "width"),
                metadataInt(media.getMetadata(), "height"),
                media.getStatus(),
                media.getResourceType(),
                media.getResourceId(),
                media.getDisplayOrder(),
                media.getDescription(),
                media.getCreatedAt(),
                media.getUpdatedAt()
        );
    }

    private static Integer metadataInt(Map<String, Object> metadata, String key) {
        if (metadata == null)
            return null;
        return metadata.get(key) instanceof Number number ? number.intValue() : null;
    }

}
