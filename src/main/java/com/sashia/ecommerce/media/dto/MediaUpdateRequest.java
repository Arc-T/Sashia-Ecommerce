package com.sashia.ecommerce.media.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

/** Only the fields that are sent (non-null) are changed. */
public record MediaUpdateRequest(
        @Nullable @Size(max = 400, message = "media.description.size") String description,
        @Nullable @Min(value = 0, message = "media.displayOrder.min") Integer displayOrder
) {
}
