package com.sashia.ecommerce.promotion.engine.pipeline.handler.target;

import com.sashia.ecommerce.promotion.target.type.TargetTypeCode;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class PromotionTargetMatcherFactory {

    private final Map<TargetTypeCode, PromotionTargetMatcher> matchers;

    public PromotionTargetMatcherFactory(List<PromotionTargetMatcher> matchers) {
        this.matchers = new LinkedHashMap<>(matchers.size());

        for (var matcher : matchers) {
            this.matchers.put(matcher.type(), matcher);
        }
    }

    public PromotionTargetMatcher get(TargetTypeCode type) {

        PromotionTargetMatcher matcher = matchers.get(type);

        if (matcher == null) {
            throw new IllegalStateException(
                    "No PromotionTargetMatcher found for type: " + type
            );
        }

        return matcher;
    }

}