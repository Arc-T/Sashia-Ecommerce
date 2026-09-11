package com.sashia.ecommerce.catalog.product.dto;

import java.math.BigDecimal;

public record ProductPriceDTO(
        BigDecimal basePrice,
        BigDecimal sellPrice,
        boolean hasDiscount
) {

    public static ProductPriceDTO toDTO(BigDecimal basePrice) {
        return new ProductPriceDTO(basePrice, basePrice, false);
    }

    public static ProductPriceDTO toDTO(BigDecimal basePrice, BigDecimal sellPrice) {
        return new ProductPriceDTO(basePrice, sellPrice, !basePrice.equals(sellPrice));
    }

}