package com.sashia.ecommerce.catalog.product.dto;

import com.sashia.ecommerce.ordering.order.CurrencyCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;

public record ProductVariantRequest(
        /* null = new variant. Ignored on create. */
        @Nullable Long id,
        @NotNull(message = "{product.variant.price.required}") @PositiveOrZero
        BigDecimal unitPrice,
        @NotNull(message = "{product.variant.currency.required}")
        CurrencyCode currency,
        @NotNull(message = "{product.variant.stock.required}") @PositiveOrZero
        Integer stock
) {
}
