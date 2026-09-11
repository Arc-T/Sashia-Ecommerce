package com.sashia.ecommerce.promotion;

import com.sashia.ecommerce.pricing.Priceable;
import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.util.List;

public interface Promotable extends Priceable {

    @Transient
    List<AppliedPromotion> getAppliedPromotions();

    void addAppliedPromotion(AppliedPromotion appliedPromotion);

    default boolean hasPromotion() {
        return !getAppliedPromotions().isEmpty();
    }

    default BigDecimal calculateTotal() {
        return calculateSubTotal().subtract(calculateTotalDiscountAmount());
    }

    default BigDecimal calculateSubTotal() {
        return getUnitPrice().multiply(BigDecimal.valueOf(getQuantity()));
    }

    default BigDecimal calculateTotalDiscountAmount() {
        return getAppliedPromotions()
                .stream()
                .map(AppliedPromotion::discountAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}