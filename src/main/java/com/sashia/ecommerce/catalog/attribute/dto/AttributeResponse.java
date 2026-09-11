package com.sashia.ecommerce.catalog.attribute.dto;

import com.sashia.ecommerce.catalog.category.dto.CategoryResponse;

import java.time.LocalDateTime;
import java.util.Set;

public record AttributeResponse(
        Long id,
        String name,
        Long categoryId,
        AttributeType type,
        Boolean isFilterable,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        // ******************************* Relations *******************************
        CategoryResponse category,
        Set<AttributeValueRequest> values
) {
}