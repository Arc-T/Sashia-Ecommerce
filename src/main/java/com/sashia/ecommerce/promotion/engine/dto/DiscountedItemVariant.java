package com.sashia.ecommerce.promotion.engine.dto;

import java.math.BigDecimal;

public record DiscountedItemVariant(
        Long itemVariantId,
        BigDecimal amount) {
}