package com.sashia.ecommerce.promotion.engine.pipeline.handler;

import org.jspecify.annotations.Nullable;

public record PromotionHandlerResult(
        boolean proceed,
        @Nullable String reason) {

    public static PromotionHandlerResult success() {
        return new PromotionHandlerResult(true, null);
    }

    public static PromotionHandlerResult failure(String reason) {
        return new PromotionHandlerResult(false, reason);
    }

}