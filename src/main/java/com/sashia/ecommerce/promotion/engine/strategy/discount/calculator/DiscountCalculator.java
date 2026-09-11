package com.sashia.ecommerce.promotion.engine.strategy.discount.calculator;

import com.sashia.ecommerce.pricing.Priceable;
import com.sashia.ecommerce.promotion.discount.Discount;

import java.math.BigDecimal;

public interface DiscountCalculator {

    BigDecimal calculate(Priceable priceable, Discount discount);

}
