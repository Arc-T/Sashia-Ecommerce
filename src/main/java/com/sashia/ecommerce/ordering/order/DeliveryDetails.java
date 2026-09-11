package com.sashia.ecommerce.ordering.order;

import com.sashia.ecommerce.catalog.item.dto.ItemDeliveryMethod;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record DeliveryDetails(
        @Enumerated(EnumType.STRING)
        ItemDeliveryMethod deliveryMethod,
        String deliveryAddress,
        String receiverName,
        String receiverPhone,
        String receiverEmail
) {
}
