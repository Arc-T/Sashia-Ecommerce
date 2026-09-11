package com.sashia.ecommerce.catalog.product.dto;

import com.sashia.ecommerce.media.dto.MediaRequest;

import java.time.LocalDateTime;
import java.util.List;

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
        List<MediaRequest> media,
//        ServiceGroupDTO serviceGroup,
        ProductPriceDTO price
) {

}