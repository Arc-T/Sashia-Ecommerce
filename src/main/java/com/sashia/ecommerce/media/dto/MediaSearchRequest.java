package com.sashia.ecommerce.media.dto;

import com.sashia.ecommerce.media.MediaResourceType;
import com.sashia.ecommerce.media.MediaStatus;
import org.jspecify.annotations.Nullable;

public record MediaSearchRequest(
        @Nullable MediaStatus status,
        @Nullable MediaResourceType resourceType,
        @Nullable Long resourceId,
        @Nullable Long ownerId,
        /* prefix match, e.g. "image/" or "image/png" */
        @Nullable String mimeType,
        /* contains match on the original file name */
        @Nullable String name
) {
}
