package com.sashia.ecommerce.promotion.engine.effect;

public sealed interface PromotionEffect permits DiscountEffect, BuyXGetYEffect, FreeShippingEffect {

    Long promotionId();

}
