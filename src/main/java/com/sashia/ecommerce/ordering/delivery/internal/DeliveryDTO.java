package com.sashia.ecommerce.ordering.delivery.internal;

import com.sashia.ecommerce.ordering.order.CurrencyCode;
import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;

import java.math.BigDecimal;
import java.util.List;

public record DeliveryDTO(
        Long id,
        String name,
        CurrencyCode currency,
        BigDecimal unitPrice,
        String description,
        List<AppliedPromotion> promotions) {
}
