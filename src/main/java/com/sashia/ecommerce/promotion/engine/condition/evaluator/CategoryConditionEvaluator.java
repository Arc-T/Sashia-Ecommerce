package com.sashia.ecommerce.promotion.engine.condition.evaluator;

import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.pricing.Priceable;
import com.sashia.ecommerce.promotion.condition.Condition;
import com.sashia.ecommerce.promotion.condition.type.ConditionTypeCode;
import com.sashia.ecommerce.promotion.engine.condition.ConditionEvaluator;
import com.sashia.ecommerce.promotion.engine.context.PromotableType;
import com.sashia.ecommerce.promotion.engine.context.PromotionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CategoryConditionEvaluator implements ConditionEvaluator {

    private static final Logger log = LoggerFactory.getLogger(CategoryConditionEvaluator.class);
    private static final PromotableType CATEGORY_CONDITION_PROMOTABLE_TYPE = PromotableType.ITEM_VARIANT;

    @Override
    public ConditionTypeCode type() {
        return ConditionTypeCode.CATEGORY_ID;
    }

    @Override
    public void evaluate(List<Condition> conditions, PromotionContext context) {

        Set<Long> categoryIds = conditions.stream()
                .map(condition -> Long.parseLong(condition.getConditionValue()))
                .collect(Collectors.toUnmodifiableSet());

        Map<Long, Priceable> matchedTargets = context.getCandidateItems(CATEGORY_CONDITION_PROMOTABLE_TYPE)
                .values()
                .stream()
                .map(priceable -> ((ItemVariant) priceable))
                .filter(itemVariant -> categoryIds.contains(itemVariant.getItem().getCategory().getId()))
                .collect(Collectors.toUnmodifiableMap(
                        ItemVariant::getId,
                        Function.identity()
                ));

        log.debug("Matched {} items for categories {}", matchedTargets, categoryIds);

        context.clearAndAddCandidates(CATEGORY_CONDITION_PROMOTABLE_TYPE, matchedTargets);
    }

}