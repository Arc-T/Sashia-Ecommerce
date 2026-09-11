package com.sashia.ecommerce.promotion.engine.pipeline.handler.target.matcher;

import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.pricing.Priceable;
import com.sashia.ecommerce.promotion.engine.context.PromotableType;
import com.sashia.ecommerce.promotion.engine.context.PromotionContext;
import com.sashia.ecommerce.promotion.engine.pipeline.handler.target.PromotionTargetMatcher;
import com.sashia.ecommerce.promotion.target.Target;
import com.sashia.ecommerce.promotion.target.type.TargetTypeCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ItemTargetMatcher implements PromotionTargetMatcher {

    private static final PromotableType ITEM_TARGET_MATCHER_PROMOTABLE_TYPE = PromotableType.ITEM_VARIANT;
    private static final Logger log = LoggerFactory.getLogger(ItemTargetMatcher.class);

    @Override
    public TargetTypeCode type() {
        return TargetTypeCode.CATEGORY;
    }

    @Override
    public void match(Set<Target> targets, PromotionContext context) {

        Set<Long> targetIds = targets.stream()
                .map(Target::getId)
                .collect(Collectors.toUnmodifiableSet());

        Map<Long, Priceable> matchedTargets = context.getCandidateItems(ITEM_TARGET_MATCHER_PROMOTABLE_TYPE)
                .values()
                .stream()
                .map(priceable -> ((ItemVariant) priceable))
                .filter(itemVariant -> targetIds.contains(itemVariant.getItem().getId()))
                .collect(Collectors.toUnmodifiableMap(
                        ItemVariant::getId,
                        Function.identity()
                ));

        context.clearAndAddCandidates(ITEM_TARGET_MATCHER_PROMOTABLE_TYPE, matchedTargets);

        log.debug("{} matched items for {}", matchedTargets.size(), targets.size());
    }

}