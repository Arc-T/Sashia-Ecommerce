package com.sashia.ecommerce.promotion.engine.condition;

import com.sashia.ecommerce.promotion.condition.type.ConditionTypeCode;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConditionEvaluatorFactory {

    private final Map<ConditionTypeCode, ConditionEvaluator> evaluators;

    public ConditionEvaluatorFactory(List<ConditionEvaluator> evaluators) {
        this.evaluators = new LinkedHashMap<>(evaluators.size());

        for (var evaluator : evaluators) {
            this.evaluators.put(evaluator.type(), evaluator);
        }
    }

    public ConditionEvaluator resolve(ConditionTypeCode type) {

        ConditionEvaluator evaluator = evaluators.get(type);

        if (evaluator == null) {
            throw new IllegalStateException(
                    "No PromotionConditionEvaluator found for type: " + type
            );
        }

        return evaluator;
    }

}