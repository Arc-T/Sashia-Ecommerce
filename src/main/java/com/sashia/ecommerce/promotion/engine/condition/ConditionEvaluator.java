package com.sashia.ecommerce.promotion.engine.condition;

import com.sashia.ecommerce.promotion.condition.Condition;
import com.sashia.ecommerce.promotion.condition.type.ConditionTypeCode;
import com.sashia.ecommerce.promotion.engine.context.PromotionContext;

import java.util.List;

public interface ConditionEvaluator {

    ConditionTypeCode type();

    void evaluate(List<Condition> conditions, PromotionContext context);

}
