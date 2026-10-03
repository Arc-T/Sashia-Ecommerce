package com.sashia.ecommerce.media.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record MediaSyncRequest(
        @NotNull(message = "media.ids.required")
        @Size(max = 50, message = "media.ids.size")
        List<Long> mediaIds
) {
}
