package com.sashia.ecommerce.promotion.engine;

import com.sashia.ecommerce.promotion.engine.dto.PromotionRequest;

/**
 * Entry point of the promotion engine.
 *
 * <p>Evaluates all active promotions against the supplied request and returns
 * the resulting promotion effects on the request.
 *
 * <p>The engine is read-only. It never persists data.
 * Its sole responsibility is to determine which promotions apply and what
 * effects they produce.
 */
public interface PromotionEngine {

    void apply(PromotionRequest request);

}
