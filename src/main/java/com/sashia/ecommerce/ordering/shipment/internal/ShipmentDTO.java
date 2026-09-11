package com.sashia.ecommerce.ordering.shipment.internal;

import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;

import java.math.BigDecimal;
import java.util.List;

public record ShipmentDTO(
        Long id,
        String name,
        BigDecimal cost,
        String description,
        List<AppliedPromotion> promotions) {
}
