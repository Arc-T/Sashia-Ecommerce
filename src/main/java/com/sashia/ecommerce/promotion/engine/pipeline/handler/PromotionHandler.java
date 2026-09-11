package com.sashia.ecommerce.promotion.engine.pipeline.handler;

import com.sashia.ecommerce.promotion.engine.context.PromotionContext;

public interface PromotionHandler {

    PromotionHandlerResult handle(PromotionContext context);

}
