package com.sashia.ecommerce.media.internal;

import com.sashia.ecommerce.media.Media;
import com.sashia.ecommerce.media.dto.MediaResponse;
import com.sashia.ecommerce.media.dto.MediaTypeEnum;

import java.util.Map;

public final class MediaMapper {

    /** Public URL prefix of the file endpoint. */
    static final String FILES_PATH = "/media/files/";

    private MediaMapper() {
    }

    public static MediaResponse toDTO(Media media) {
        return new MediaResponse(
                media.getId(),
                FILES_PATH + media.getSlug(),
                media.getOriginalName(),
                media.getMimeType(),
                media.getExtension(),
                kindOf(media.getMimeType()),
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

    private static MediaTypeEnum kindOf(String mimeType) {
        if (mimeType == null)
            return null;
        if (mimeType.startsWith("image/"))
            return MediaTypeEnum.IMAGE;
        if (mimeType.startsWith("video/"))
            return MediaTypeEnum.VIDEO;
        if (mimeType.startsWith("audio/"))
            return MediaTypeEnum.AUDIO;
        return null;
    }

    private static Integer metadataInt(Map<String, Object> metadata, String key) {
        if (metadata == null)
            return null;
        return metadata.get(key) instanceof Number number ? number.intValue() : null;
    }

}
