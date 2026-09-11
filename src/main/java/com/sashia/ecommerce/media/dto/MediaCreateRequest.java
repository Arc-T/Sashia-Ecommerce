package com.sashia.ecommerce.media.dto;

import org.jspecify.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record MediaCreateRequest(
        @Nullable Long productId,
        MultipartFile[] media
) {
}