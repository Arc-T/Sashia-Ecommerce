package com.sashia.ecommerce.catalog.product.dto;

import com.sashia.ecommerce.catalog.item.dto.ItemType;

import java.time.LocalDateTime;

public record ProductSummary(
        Long id,
        String title,
        ItemType itemType,
        ProductPriceDTO discount,
        String coverImage,
        boolean isFeatured,
        LocalDateTime createdAt
) {

}