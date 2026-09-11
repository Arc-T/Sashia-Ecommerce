package com.sashia.ecommerce.promotion.engine.strategy.discount.calculator;

import com.sashia.ecommerce.pricing.Priceable;
import com.sashia.ecommerce.promotion.discount.Discount;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class PercentDiscountCalculator implements DiscountCalculator {

    @Override
    public BigDecimal calculate(Priceable priceable, Discount discount) {
        return priceable
            .getUnitPrice()
            .multiply(discount.getAmount())
            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
