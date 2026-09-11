package com.sashia.ecommerce.promotion.engine.effect;

import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.promotion.Promotion;
import com.sashia.ecommerce.promotion.engine.context.PromotableType;
import com.sashia.ecommerce.promotion.engine.context.PromotionContext;
import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PromotionEffectApplierImpl implements PromotionEffectApplier {

    private static final Logger log = LoggerFactory.getLogger(PromotionEffectApplierImpl.class);
    private static final PromotableType DISCOUNT_PROMOTION_EFFECT_PROMOTABLE_TYPE = PromotableType.ITEM_VARIANT;

    @Override
    public void apply(PromotionContext context) {

        log.debug("Applicable items: {}", context.getCandidates());

        for (PromotionEffect effect : context.getEffects()) {
            applyEffect(effect, context);
        }
    }

    private void applyEffect(PromotionEffect effect, PromotionContext context) {
        switch (effect) {
            case FreeShippingEffect shippingEffect -> IO.println(shippingEffect);
            case BuyXGetYEffect buyXGetYEffect -> IO.println(buyXGetYEffect);
            case DiscountEffect discountEffect -> applyDiscountEffect(discountEffect, context);
            default -> throw new IllegalArgumentException("Unsupported promotion effect: "
                    + effect.getClass().getSimpleName()
            );
        }
    }

    private void applyDiscountEffect(DiscountEffect effect, PromotionContext context) {

        Promotion promotion = context.getPromotion();

        log.debug("Discounted items: {}", effect.discountedItemVariants());

        Map<Long, ItemVariant> priceableItemVariants = context.getCandidateItems(DISCOUNT_PROMOTION_EFFECT_PROMOTABLE_TYPE)
                .entrySet()
                .stream()
                .collect(
                        Collectors.toUnmodifiableMap(
                                Map.Entry::getKey,
                                priceable -> (ItemVariant) priceable.getValue()
                        )
                );

        for (var discountedItemVariant : effect.discountedItemVariants()) {

            ItemVariant itemVariant = priceableItemVariants.get(discountedItemVariant.itemVariantId());

            BigDecimal priceAfter = itemVariant.getUnitPrice().subtract(discountedItemVariant.amount());

            itemVariant.addAppliedPromotion(
                    new AppliedPromotion(
                            promotion,
                            discountedItemVariant.amount(),
                            itemVariant.getUnitPrice(),
                            priceAfter
                    )
            );
        }

    }

}