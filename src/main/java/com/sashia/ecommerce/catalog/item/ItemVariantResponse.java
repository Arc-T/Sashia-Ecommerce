package com.sashia.ecommerce.catalog.item;

import com.sashia.ecommerce.catalog.item.dto.PriceResponse;

public record ItemVariantResponse(
        Long id,
        Integer quantity,
        PriceResponse priceable) {
}
