package com.sashia.ecommerce.media.dto;

import com.sashia.ecommerce.media.internal.MediaType;

public record MediaTypeResponse(
        Long id,
        String name,
        String description
) {
    // ********************** DTOs **********************
    public static MediaTypeResponse toDTO(MediaType mediaType) {
        return new MediaTypeResponse(
                mediaType.id(),
                mediaType.name(),
                mediaType.description()
        );
    }

}
