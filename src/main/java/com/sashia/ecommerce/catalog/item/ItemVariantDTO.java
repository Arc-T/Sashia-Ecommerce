package com.sashia.ecommerce.catalog.item;

import com.sashia.ecommerce.catalog.item.dto.PriceableDTO;

public record ItemVariantDTO(
        Long id,
        Integer quantity,
        PriceableDTO priceable) {
}
