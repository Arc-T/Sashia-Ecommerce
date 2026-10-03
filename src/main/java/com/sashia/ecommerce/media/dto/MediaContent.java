package com.sashia.ecommerce.media.dto;

import org.springframework.core.io.Resource;

public record MediaContent(
        Resource resource,
        String fileName,
        String mimeType,
        long size,
        String checksum,
        /* true: SAVED media, cacheable by everyone. false: draft, private */
        boolean publicContent
) {
}
