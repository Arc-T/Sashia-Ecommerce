package com.sashia.ecommerce.ordering.order;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

@Embeddable
public record PricingDetails(
        @Enumerated(EnumType.STRING)
        CurrencyCode currency,
        BigDecimal subtotal,
        BigDecimal deliveryCost,
        BigDecimal tax,
        BigDecimal additionalCharges,
        BigDecimal discountAmount,
        BigDecimal total) {

}
