package com.sashia.ecommerce.promotion.engine.strategy.discount.calculator;

import com.sashia.ecommerce.pricing.Priceable;
import com.sashia.ecommerce.promotion.discount.Discount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FixedDiscountCalculator implements DiscountCalculator {

    @Override
    public BigDecimal calculate(Priceable priceable, Discount discount) {
        return discount.getAmount();
    }

}