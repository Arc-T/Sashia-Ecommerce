package com.sashia.ecommerce.catalog.item.dto;

import com.sashia.ecommerce.catalog.item.ItemVariantResponse;

import java.util.List;

public record ItemSummaryResponse(
        Long id,
        String title,
        Long categoryId,
        List<ItemVariantResponse> itemVariants) {
}
