package com.sashia.ecommerce.promotion.engine.pipeline.handler.target;

import com.sashia.ecommerce.promotion.engine.context.PromotionContext;
import com.sashia.ecommerce.promotion.target.Target;
import com.sashia.ecommerce.promotion.target.type.TargetTypeCode;

import java.util.Set;

public interface PromotionTargetMatcher {

    TargetTypeCode type();

    void match(Set<Target> targets, PromotionContext context);

}