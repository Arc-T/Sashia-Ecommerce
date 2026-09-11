package com.sashia.ecommerce.billing.payment.dto;

import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;

import java.math.BigDecimal;
import java.util.List;

public record PaymentMethodDTO(
        Long id,
        String name,
        BigDecimal cost,
        String description,
        List<AppliedPromotion> promotions) {
}
