package com.sashia.ecommerce.catalog.category.dto;

import java.time.LocalDateTime;

public record CategoryResponse(
        Long id,
        String name,
        String url,
        String icon,
        Long parentId,
        Integer displayOrder,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
