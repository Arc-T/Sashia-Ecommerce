package com.sashia.ecommerce.catalog.item.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppliedPromotionResponse(
        Long id,
        String name,
        LocalDateTime validFrom,
        LocalDateTime validUntil,
        String description,
        BigDecimal discountAmount,
        BigDecimal priceBefore,
        BigDecimal priceAfter) {
}