package com.sashia.ecommerce.promotion.engine.strategy;

import com.sashia.ecommerce.promotion.engine.context.PromotionContext;
import com.sashia.ecommerce.promotion.engine.dto.PromotionRequest;
import com.sashia.ecommerce.promotion.type.TypeCode;

public interface PromotionStrategy<T extends PromotionRequest> {

    TypeCode type();

    void execute(PromotionContext context);

    T getPromotionRequest(PromotionContext context);

}
