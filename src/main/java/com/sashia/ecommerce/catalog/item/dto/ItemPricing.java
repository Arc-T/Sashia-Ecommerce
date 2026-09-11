package com.sashia.ecommerce.catalog.item.dto;

import com.sashia.ecommerce.ordering.order.CurrencyCode;
import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemPricing {

    private final BigDecimal basePrice;

    private final CurrencyCode currency;

    /* **************** Mutable fields ***************** */

    private BigDecimal discountedPrice;

    private BigDecimal discountAmount = BigDecimal.ZERO;

    private List<AppliedPromotion> promotions = new ArrayList<>();

    public ItemPricing(BigDecimal basePrice, CurrencyCode currency) {
        this.basePrice = basePrice;
        this.currency = currency;
        this.discountedPrice = basePrice.subtract(discountAmount);
    }

    /* ********************************** HELPERS ************************************* */

    public boolean hasPromotion() {
        return !this.getPromotions().isEmpty();
    }

    private void calculateDiscountedPrice() {
        if (!promotions.isEmpty()) {

            setDiscountedPrice(promotions.stream()
                    .map(AppliedPromotion::discountAmount)
                    .reduce(getBasePrice(), BigDecimal::subtract)
            );

            setDiscountAmount(promotions.stream()
                    .map(AppliedPromotion::discountAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
            );
        }
    }

    /* ****************************** GETTER & SETTERS ******************************** */

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    private void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public List<AppliedPromotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<AppliedPromotion> promotions) {
        this.promotions = promotions;
    }

}
