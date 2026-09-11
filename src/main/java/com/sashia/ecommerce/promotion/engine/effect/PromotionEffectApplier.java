package com.sashia.ecommerce.promotion.engine.effect;

import com.sashia.ecommerce.promotion.engine.context.PromotionContext;

public interface PromotionEffectApplier {

    void apply(PromotionContext context);

}