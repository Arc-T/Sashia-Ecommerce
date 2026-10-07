package com.sashia.ecommerce.catalog.product.dto;

import com.sashia.ecommerce.catalog.item.ItemVariantResponse;
import com.sashia.ecommerce.media.dto.MediaResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record ProductResponse(
        Long id,
        String title,
        Long categoryId,
        boolean featured,
        String description,
        Set<Long> tagIds,
        List<ItemVariantResponse> variants,
        List<MediaResponse> media,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}