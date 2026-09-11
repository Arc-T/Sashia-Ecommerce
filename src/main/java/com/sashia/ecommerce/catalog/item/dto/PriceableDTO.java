package com.sashia.ecommerce.catalog.item.dto;

import com.sashia.ecommerce.ordering.order.CurrencyCode;

import java.math.BigDecimal;
import java.util.List;

public record PriceableDTO(
        BigDecimal unitPrice,
        BigDecimal subTotal,
        BigDecimal totalDiscountAmount,
        BigDecimal total,
        CurrencyCode currency,
        List<AppliedPromotionDTO> promotions) {
}
