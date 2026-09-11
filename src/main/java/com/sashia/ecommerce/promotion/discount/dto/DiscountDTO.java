package com.sashia.ecommerce.promotion.discount.dto;

import com.sashia.ecommerce.promotion.discount.type.DiscountTypeCode;

import java.math.BigDecimal;

public record DiscountDTO(
        BigDecimal amount,
        DiscountTypeCode type
) {
}
