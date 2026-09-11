package com.sashia.ecommerce.ordering.order.dto;

import org.jspecify.annotations.NonNull;

public record ItemDeliveryDto(
        @NonNull Long shipmentId,
        String address,
        String receiverName,
        String receiverPhone,
        String receiverEmail) {
}
