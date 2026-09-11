package com.sashia.ecommerce.promotion.engine.effect;

import com.sashia.ecommerce.promotion.engine.dto.DiscountedItemVariant;

import java.util.List;

/**
 * Represents all discounts produced by a single promotion.
 *
 * <p>The map key is the affected item identifier and the value is the
 * discount amount that should be applied to that item.
 */
public record DiscountEffect(
        Long promotionId,
        List<DiscountedItemVariant> discountedItemVariants) implements PromotionEffect {
}