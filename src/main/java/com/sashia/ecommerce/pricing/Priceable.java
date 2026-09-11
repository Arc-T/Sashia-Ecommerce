package com.sashia.ecommerce.pricing;

import com.sashia.ecommerce.ordering.order.CurrencyCode;
import jakarta.persistence.Transient;

import java.math.BigDecimal;

public interface Priceable {

    @Transient
    Integer getQuantity();

    BigDecimal getUnitPrice();

    CurrencyCode getCurrency();

}
