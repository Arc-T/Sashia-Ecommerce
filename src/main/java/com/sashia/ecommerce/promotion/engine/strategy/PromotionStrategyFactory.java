package com.sashia.ecommerce.promotion.engine.strategy;

import com.sashia.ecommerce.promotion.engine.dto.PromotionRequest;
import com.sashia.ecommerce.promotion.type.TypeCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class PromotionStrategyFactory {

    private static final Logger log = LoggerFactory.getLogger(PromotionStrategyFactory.class);

    private final Map<TypeCode, PromotionStrategy<? extends PromotionRequest>> promotionStrategies;

    public PromotionStrategyFactory(List<PromotionStrategy<? extends PromotionRequest>> promotionStrategies) {
        this.promotionStrategies = new LinkedHashMap<>(promotionStrategies.size());

        for (var strategy : promotionStrategies) {
            this.promotionStrategies.put(strategy.type(), strategy);
        }
    }

    public PromotionStrategy<? extends PromotionRequest> resolve(TypeCode type) {

        PromotionStrategy<? extends PromotionRequest> strategy = promotionStrategies.get(type);

        log.debug("PromotionStrategy found for type {}", type);

        if (strategy == null) {
            throw new IllegalStateException(
                    "No PromotionStrategy found for type: " + type
            );
        }
        return strategy;
    }

}
