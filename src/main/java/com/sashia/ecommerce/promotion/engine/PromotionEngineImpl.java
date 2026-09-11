package com.sashia.ecommerce.promotion.engine;

import com.sashia.ecommerce.promotion.Promotion;
import com.sashia.ecommerce.promotion.engine.context.PromotionContext;
import com.sashia.ecommerce.promotion.engine.dto.PromotionRequest;
import com.sashia.ecommerce.promotion.engine.effect.PromotionEffectApplier;
import com.sashia.ecommerce.promotion.engine.pipeline.PromotionPipeline;
import com.sashia.ecommerce.promotion.engine.resolver.PromotionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Default implementation of the promotion engine.
 *
 * <p>The engine resolves all applicable promotions for the incoming request,
 * evaluates each promotion independently through the promotion pipeline and
 * aggregates the produced effects into a single {@link com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion}.
 */
@Component
public class PromotionEngineImpl implements PromotionEngine {

    private static final Logger log = LoggerFactory.getLogger(PromotionEngineImpl.class);

    private final PromotionResolver promotionResolver;
    private final PromotionPipeline promotionPipeline;
    private final PromotionEffectApplier promotionEffectApplier;

    public PromotionEngineImpl(PromotionResolver promotionResolver, PromotionPipeline promotionPipeline, PromotionEffectApplier promotionEffectApplier) {
        this.promotionResolver = promotionResolver;
        this.promotionPipeline = promotionPipeline;
        this.promotionEffectApplier = promotionEffectApplier;
    }

    @Override
    public void apply(PromotionRequest request) {

        List<Promotion> promotions = promotionResolver.resolve();

        log.debug("Entered engine with promotions: {}", promotions);

        if (!promotions.isEmpty()) {

            for (Promotion promotion : promotions) {

                PromotionContext context = new PromotionContext(promotion, request);

                promotionPipeline.execute(context);

                promotionEffectApplier.apply(context);
            }
        }
    }

}