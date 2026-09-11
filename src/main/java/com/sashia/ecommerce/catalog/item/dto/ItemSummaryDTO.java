package com.sashia.ecommerce.catalog.item.dto;

import com.sashia.ecommerce.catalog.item.ItemVariantDTO;

import java.util.List;

public record ItemSummaryDTO(
        Long id,
        String title,
        Long categoryId,
        List<ItemVariantDTO> itemVariants) {
}
