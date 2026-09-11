package com.sashia.ecommerce.promotion.engine.pipeline.handler;

import com.sashia.ecommerce.promotion.Promotion;
import com.sashia.ecommerce.promotion.condition.Condition;
import com.sashia.ecommerce.promotion.condition.type.ConditionTypeCode;
import com.sashia.ecommerce.promotion.engine.condition.ConditionEvaluator;
import com.sashia.ecommerce.promotion.engine.condition.ConditionEvaluatorFactory;
import com.sashia.ecommerce.promotion.engine.context.PromotionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Evaluates all promotion conditions.
 *
 * <p>Conditions are grouped by their type and delegated to the corresponding
 * {@link ConditionEvaluator}. Each evaluator determines whether its
 * group of conditions is satisfied.
 *
 * <p>Different condition types are evaluated using AND semantics. If any
 * evaluator fails, the promotion pipeline is terminated.
 */
@Component
@Order(value = 3)
public class ConditionHandler implements PromotionHandler {

    private static final Logger log = LoggerFactory.getLogger(ConditionHandler.class);

    private final ConditionEvaluatorFactory evaluatorFactory;

    public ConditionHandler(ConditionEvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    @Override
    public PromotionHandlerResult handle(PromotionContext context) {

        Promotion promotion = context.getPromotion();

        if (!promotion.getConditions().isEmpty()) {

            Map<ConditionTypeCode, List<Condition>> groupedConditions = groupConditionsByType(promotion);

            applyConditions(groupedConditions, context);

            if (context.isCandidatesEmpty())
                return PromotionHandlerResult.failure("Condition not satisfied");
        }

        log.debug("Promotion conditions satisfied by {} conditions", promotion.getConditions().size());

        return PromotionHandlerResult.success();

    }

    private Map<ConditionTypeCode, List<Condition>> groupConditionsByType(Promotion promotion) {
        return promotion.getConditions()
                .stream()
                .collect(Collectors.groupingBy(
                        Condition::getConditionTypeCode,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    private void applyConditions(Map<ConditionTypeCode, List<Condition>> groupedConditions,
                                 PromotionContext context) {

        for (Map.Entry<ConditionTypeCode, List<Condition>> entry : groupedConditions.entrySet()) {

            ConditionEvaluator evaluator = evaluatorFactory.resolve(entry.getKey());

            log.debug("Promotion condition evaluator: {}", evaluator);

            evaluator.evaluate(entry.getValue(), context);
        }

    }

}