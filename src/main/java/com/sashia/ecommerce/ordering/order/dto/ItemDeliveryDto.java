package com.sashia.ecommerce.ordering.order.dto;

import org.jspecify.annotations.NonNull;

public record ItemDeliveryDto(
        @NonNull Long deliveryOptionId,
        String address,
        String receiverName,
        String receiverPhone,
        String receiverEmail) {
}
