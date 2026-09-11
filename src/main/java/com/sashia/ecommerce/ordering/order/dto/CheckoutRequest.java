package com.sashia.ecommerce.ordering.order.dto;

import com.sashia.ecommerce.billing.payment.dto.PaymentMethodDTO;
import com.sashia.ecommerce.catalog.item.dto.ItemSummaryDTO;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

public record CheckoutRequest(
        @Nullable String coupon,
        @Nullable String description,
        @NonNull ItemDeliveryDto delivery,
        @NonNull List<ItemSummaryDTO> items,
        @NonNull PaymentMethodDTO paymentMethod,
        @NonNull BigDecimal totalPrice) {
}
