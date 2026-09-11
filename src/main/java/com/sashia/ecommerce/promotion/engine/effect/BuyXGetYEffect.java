package com.sashia.ecommerce.promotion.engine.effect;

public record BuyXGetYEffect(
        Long promotionId,
        Long purchasedItemId,
        Long rewardedItemId,
        Integer rewardedQuantity
) implements PromotionEffect {
}
