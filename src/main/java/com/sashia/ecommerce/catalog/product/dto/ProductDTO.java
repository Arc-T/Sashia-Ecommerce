package com.sashia.ecommerce.catalog.product.dto;

import java.time.LocalDateTime;

public record ProductDTO(
        Long id,
        String name,
        Long shopId,
        Integer stock,
        ProductStatus status,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        // ===================================== RELATIONS =====================================
//        CategoryDTO category,
//        List<MediaRequest> media,
//        ServiceGroupDTO serviceGroup,
        ProductPriceDTO price
) {

}