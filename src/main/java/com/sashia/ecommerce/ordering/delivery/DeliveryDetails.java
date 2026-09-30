package com.sashia.ecommerce.ordering.delivery;

import com.sashia.ecommerce.ordering.delivery.option.DeliveryMethod;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record DeliveryDetails(
        @Enumerated(EnumType.STRING)
        DeliveryMethod deliveryMethod,
        String deliveryAddress,
        String receiverName,
        String receiverPhone,
        String receiverEmail
) {
}
