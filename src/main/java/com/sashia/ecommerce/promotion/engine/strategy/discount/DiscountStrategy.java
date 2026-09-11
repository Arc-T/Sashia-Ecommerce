package com.sashia.ecommerce.promotion.engine.strategy.discount;

import com.sashia.ecommerce.pricing.Priceable;
import com.sashia.ecommerce.promotion.discount.Discount;
import com.sashia.ecommerce.promotion.engine.context.PromotableType;
import com.sashia.ecommerce.promotion.engine.context.PromotionContext;
import com.sashia.ecommerce.promotion.engine.dto.CartPromotionRequest;
import com.sashia.ecommerce.promotion.engine.dto.DiscountedItemVariant;
import com.sashia.ecommerce.promotion.engine.effect.DiscountEffect;
import com.sashia.ecommerce.promotion.engine.strategy.PromotionStrategy;
import com.sashia.ecommerce.promotion.engine.strategy.discount.calculator.DiscountCalculator;
import com.sashia.ecommerce.promotion.engine.strategy.discount.calculator.FixedDiscountCalculator;
import com.sashia.ecommerce.promotion.engine.strategy.discount.calculator.PercentDiscountCalculator;
import com.sashia.ecommerce.promotion.type.TypeCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Produces a {@link DiscountEffect} for a discount promotion.
 *
 * <p>The strategy calculates the discount amount for every applicable item
 * and aggregates the results into a single effect. No item prices are
 * modified here; the generated effect is applied later by the
 * {@code PromotionEffectApplier}.
 */
@Component
public class DiscountStrategy implements PromotionStrategy<CartPromotionRequest> {

    private static final Logger log = LoggerFactory.getLogger(DiscountStrategy.class);

    private final FixedDiscountCalculator fixedDiscountCalculator;
    private final PercentDiscountCalculator percentDiscountCalculator;

    public DiscountStrategy(FixedDiscountCalculator fixedDiscountCalculator, PercentDiscountCalculator percentDiscountCalculator) {
        this.fixedDiscountCalculator = fixedDiscountCalculator;
        this.percentDiscountCalculator = percentDiscountCalculator;
    }

    @Override
    public TypeCode type() {
        return TypeCode.DISCOUNT;
    }

    @Override
    public void execute(PromotionContext context) {

        Discount discount = context.getPromotion().getDiscount();

        DiscountCalculator calculator = switch (discount.getType().getCode()) {
            case FIXED -> fixedDiscountCalculator;
            case PERCENT -> percentDiscountCalculator;
        };

        List<DiscountedItemVariant> discountedItemVariants = new ArrayList<>();

        log.debug("Candidates for discount: {}", context.getCandidateItems(PromotableType.ITEM_VARIANT).entrySet());

        for (Map.Entry<Long, Priceable> candidate : context.getCandidateItems(PromotableType.ITEM_VARIANT).entrySet()) {

            BigDecimal discountAmount = calculator.calculate(candidate.getValue(), discount);

            log.debug("Discounted item: {}", candidate.getValue());
            log.debug("Discounted amount: {}", discountAmount);

            discountedItemVariants.add(
                    new DiscountedItemVariant(
                            candidate.getKey(),
                            discountAmount
                    )
            );
        }

        context.addEffect(
                new DiscountEffect(
                        context.getPromotion().getId(),
                        discountedItemVariants
                )
        );
    }

    @Override
    public CartPromotionRequest getPromotionRequest(PromotionContext context) {
        return (CartPromotionRequest) context.getRequest();
    }

}